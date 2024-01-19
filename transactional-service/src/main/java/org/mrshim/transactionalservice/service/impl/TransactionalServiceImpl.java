package org.mrshim.transactionalservice.service.impl;

import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.mrshim.foodhub.PaymentNotificationMessage;
import org.mrshim.transactionalservice.dto.OrderResponse;
import org.mrshim.transactionalservice.dto.PaymentRequest;
import org.mrshim.foodhub.PaymentEvent;
import org.mrshim.transactionalservice.exception.PaymentException;
import org.mrshim.transactionalservice.mapper.TransactionalMapper;
import org.mrshim.transactionalservice.service.ConvertCurrencyService;
import org.mrshim.transactionalservice.service.TransactionalService;
import org.mrshim.transactionalservice.service.communication.CommunicationKafkaService;
import org.mrshim.transactionalservice.service.communication.OrderTransactionService;
import org.mrshim.transactionalservice.stripe.StripeClient;
import org.springframework.kafka.KafkaException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionalServiceImpl implements TransactionalService {

    private final StripeClient stripeClient;
    private final OrderTransactionService orderTransactionService;
    private final ConvertCurrencyService convertCurrencyService;
    private final CommunicationKafkaService communicationKafkaService;
    private final TransactionalMapper transactionalMapper;

    public String charge(PaymentRequest paymentRequest, String token) {

        Mono<OrderResponse> order = orderTransactionService.getOrder(paymentRequest.getId(), token);
        OrderResponse orderResponse = order.block();

        if (orderResponse == null || !orderResponse.getStatus().equals("Принят")) {
            throw new PaymentException("Ошибка заказа");
        }

        double v = convertCurrencyService.ConvertCurrency(orderResponse.getOrderAmount());


        Charge charge = null;
        try {
            charge = stripeClient.chargeNewCard(paymentRequest.getToken(), v);
        } catch (Exception e) {
            throw new PaymentException("Ошибка оплаты");
        }

        try {
            PaymentNotificationMessage paymentNotificationMessage = transactionalMapper.mapOrderResponseToPaymentNotificationMessage(orderResponse, paymentRequest.getEmail());
            communicationKafkaService.sendNotification(paymentNotificationMessage);
            PaymentEvent paymentEvent = new PaymentEvent();
            paymentEvent.setId(paymentRequest.getId());
            communicationKafkaService.sendPayment(paymentEvent);
            return charge.getId();
        } catch (KafkaException e) {
            throw new PaymentException(e.getMessage());
        }
    }
}
