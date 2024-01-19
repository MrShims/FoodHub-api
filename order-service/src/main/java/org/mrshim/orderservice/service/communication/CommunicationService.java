package org.mrshim.orderservice.service.communication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.mrshim.foodhub.PaymentEvent;
import org.mrshim.orderservice.enums.OrderStatusEnum;
import org.mrshim.orderservice.model.Order;
import org.mrshim.orderservice.model.OrderStatus;
import org.mrshim.orderservice.repository.OrderRepository;
import org.mrshim.orderservice.repository.OrderStatusRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunicationService {
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;

    @KafkaListener(topics = "transactional.payment", groupId = "order", containerFactory = "kafkaListenerContainerFactory")
    public void listen(PaymentEvent paymentEvent, Acknowledgment acknowledgment) {
        Optional<Order> byId = orderRepository.findById(paymentEvent.getId());
        if (byId.isPresent()) {
            Order order = byId.get();
            if (order.getOrderStatus().getStatusName().equals(OrderStatusEnum.ACCEPT)) {
                Optional<OrderStatus> byStatusName = orderStatusRepository.findByStatusName(OrderStatusEnum.PAID);
                if (byStatusName.isPresent()) {
                    order.setOrderStatus(byStatusName.get());
                    orderRepository.save(order);
                } else {
                    throw new RuntimeException();
                }
            }
            log.info("Mistake, the order status with id - {} cannot be updated", paymentEvent.getId());
        } else {
            log.info("Order with id - {} not found", paymentEvent.getId());
        }
        acknowledgment.acknowledge();
    }
}
