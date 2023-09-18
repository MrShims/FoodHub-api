package org.mrshim.notificationservice.consumer;

import lombok.RequiredArgsConstructor;
import org.mrshim.notificationservice.dto.OrderPlacedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final JavaMailSender javaMailSender;



    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderKafkaResponseDto) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom("no-reply@example.com");
        simpleMailMessage.setTo(orderKafkaResponseDto.getUserEmail());
        simpleMailMessage.setSubject("Ваш заказ "+orderKafkaResponseDto.getId());
        simpleMailMessage.setText("Ваш заказ на сумму "+orderKafkaResponseDto.getOrderAmount());
        javaMailSender.send(simpleMailMessage);



    }


}
