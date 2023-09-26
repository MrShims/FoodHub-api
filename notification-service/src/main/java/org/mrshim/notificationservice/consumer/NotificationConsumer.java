package org.mrshim.notificationservice.consumer;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.mrshim.notificationservice.dto.OrderLineDish;
import org.mrshim.notificationservice.dto.OrderPlacedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final JavaMailSender javaMailSender;




    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderKafkaResponseDto) {



        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            helper.setFrom("no-reply@example.com");
            helper.setTo(orderKafkaResponseDto.getEmail());
            helper.setSubject("Ваш заказ "+orderKafkaResponseDto.getId());
            String htmlContent = "<html><body>" +
                    "<h2>Уважаемый клиент,</h2>" +
                    "<p>Ваш заказ №" + orderKafkaResponseDto.getId() + " был успешно создан. Вот детали заказа:</p>" +
                    "<ul>" +
                    "<li><strong>Сумма заказа:</strong> $" + orderKafkaResponseDto.getOrderAmount() + "</li>" +
                    "<li><strong>Адрес доставки:</strong> " + orderKafkaResponseDto.getDeliveryAddress() + "</li>" +
                    "<li><strong>Список заказов:</strong></li>";



            for (OrderLineDish lineDish : orderKafkaResponseDto.getLineDishes()) {
                htmlContent += "<li>" + lineDish.getName() + " (количество: " + lineDish.getQuantity() + ")</li>";
            }

            htmlContent += "</ul>" +
                    "<p>Спасибо за ваш заказ!</p>" +
                    "</body></html>";


            helper.setText(htmlContent, true);



            //javaMailSender.send(mimeMessage);


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }







    }


}
