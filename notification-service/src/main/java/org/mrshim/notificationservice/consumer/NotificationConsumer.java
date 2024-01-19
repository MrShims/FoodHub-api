package org.mrshim.notificationservice.consumer;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.mrshim.foodhub.OrderListMessage;
import org.mrshim.foodhub.PaymentNotificationMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "transactional.notification",containerFactory = "kafkaListenerContainerFactory")
    public void handleNotification(PaymentNotificationMessage paymentNotificationMessage, Acknowledgment acknowledgment) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            helper.setFrom("no-reply@example.com");
            helper.setTo(safeToString(paymentNotificationMessage.getEmail()));
            helper.setSubject("Ваш заказ " + paymentNotificationMessage.getId());
            String htmlContent = "<html><body>" +
                    "<h2>Уважаемый клиент,</h2>" +
                    "<p>Ваш заказ №" + paymentNotificationMessage.getId() + " был успешно создан. Вот детали заказа:</p>" +
                    "<ul>" +
                    "<li><strong>Сумма заказа:</strong> $" + paymentNotificationMessage.getOrderAmount() + "</li>" +
                    "<li><strong>Адрес доставки:</strong> " + paymentNotificationMessage.getDeliveryAddress() + "</li>" +
                    "<li><strong>Список заказов:</strong></li>";
            for (OrderListMessage lineDish : paymentNotificationMessage.getLineDishes()) {
                htmlContent += "<li>" + lineDish.getName() + " (количество: " + lineDish.getQuantity() + ")</li>";
            }
            htmlContent += "</ul>" +
                    "<p>Спасибо за ваш заказ!</p>" +
                    "</body></html>";
            helper.setText(htmlContent, true);

            //javaMailSender.send(mimeMessage);
            acknowledgment.acknowledge();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String safeToString(CharSequence input) {
        return input == null ? null : input.toString();
    }
}
