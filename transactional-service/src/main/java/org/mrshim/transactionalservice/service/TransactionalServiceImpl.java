package org.mrshim.transactionalservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.mrshim.transactionalservice.client.OrderTransactionClient;
import org.mrshim.transactionalservice.dto.OrderPaymentEvent;
import org.mrshim.transactionalservice.dto.OrderResponse;
import org.mrshim.transactionalservice.dto.PaymentRequest;
import org.mrshim.transactionalservice.dto.RabbitMessage;
import org.mrshim.transactionalservice.exception.PaymentException;
import org.mrshim.transactionalservice.stripe.StripeClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionalServiceImpl implements TransactionalService {

    private final StripeClient stripeClient;

    private final OrderTransactionClient orderTransactionClient;

    private final ConvertCurrencyService convertCurrencyService;

    private final KafkaTemplate<String, OrderPaymentEvent> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public String charge(PaymentRequest paymentRequest, String token) {


        Mono<OrderResponse> order = orderTransactionClient.getOrder(paymentRequest.getId(), token);
        OrderResponse orderResponse = order.block();

        if (!orderResponse.getStatus().equals("Принят")) {
            throw new PaymentException("Ошибка заказа");
        }

        double v = convertCurrencyService.ConvertCurrency(orderResponse.getOrderAmount());


        try {


            OrderPaymentEvent orderPaymentEvent = mapOrderResponseToOrderPaymentEvent(orderResponse, paymentRequest.getEmail());

            Charge charge = stripeClient.chargeNewCard(paymentRequest.getToken(), v);

            kafkaTemplate.send("notificationTopic", orderPaymentEvent);

            RabbitMessage rabbitMessage=new RabbitMessage();
            rabbitMessage.setId(orderPaymentEvent.getId());

            String message = objectMapper.writeValueAsString(rabbitMessage);
            rabbitTemplate.convertAndSend(exchange,routingKey,message);



            return charge.getId();


        } catch (Exception e) {

            throw new PaymentException("Ошибка оплаты");
        }


    }


    private OrderPaymentEvent mapOrderResponseToOrderPaymentEvent(OrderResponse orderResponse, String email) {
        OrderPaymentEvent orderPaymentEvent = new OrderPaymentEvent();

        orderPaymentEvent.setOrderAmount(orderResponse.getOrderAmount());
        orderPaymentEvent.setId(orderResponse.getId());
        orderPaymentEvent.setEmail(email);
        orderPaymentEvent.setDeliveryAddress(orderResponse.getDeliveryAddress());
        orderPaymentEvent.setLineDishes(orderResponse.getLineDishes());

        return orderPaymentEvent;


    }


}
