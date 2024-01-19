package org.mrshim.transactionalservice.service.communication;

import org.mrshim.transactionalservice.properties.KafkaProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommunicationKafkaService {

    private final KafkaTemplate<String, org.mrshim.foodhub.PaymentNotificationMessage> kafkaTemplate1;
    private final KafkaTemplate<String, org.mrshim.foodhub.PaymentEvent> kafkaTemplate2;
    private final KafkaProperties kafkaProperties;

    public CommunicationKafkaService(@Qualifier("kafkaPaymentNotificationTemplate") KafkaTemplate<String, org.mrshim.foodhub.PaymentNotificationMessage> kafkaTemplate1,
                                     @Qualifier("kafkaPaymentEventTemplate") KafkaTemplate<String, org.mrshim.foodhub.PaymentEvent> kafkaTemplate2,
                                     KafkaProperties kafkaProperties) {
        this.kafkaTemplate1 = kafkaTemplate1;
        this.kafkaTemplate2 = kafkaTemplate2;
        this.kafkaProperties = kafkaProperties;
    }

    public void sendNotification(org.mrshim.foodhub.PaymentNotificationMessage notificationMessage) {
        kafkaTemplate1.send(kafkaProperties.getOutputNotificationTopic(), notificationMessage);
    }

    public void sendPayment(org.mrshim.foodhub.PaymentEvent paymentMessage) {
        kafkaTemplate2.send(kafkaProperties.getOutputPaymentTopic(), paymentMessage);
    }
}
