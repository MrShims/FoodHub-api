package org.mrshim.notificationservice.consumer;

import org.mrshim.notificationservice.dto.OrderKafkaResponseDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/*@Service
public class NotificationConsumer {


    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderKafkaResponseDto orderKafkaResponseDto) {

        System.out.println(orderKafkaResponseDto.toString());

    }


}*/
