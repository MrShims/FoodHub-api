package org.mrshim.transactionalservice.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.mrshim.transactionalservice.properties.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {
    private final KafkaProperties kafkaProperties;

    @Bean
    public NewTopic notificationTopic()
    {
        return TopicBuilder.name(kafkaProperties.getOutputNotificationTopic()).partitions(kafkaProperties.getOutputNotificationTopicPartitions()).build();
    }
    @Bean
    public NewTopic paymentTopic()
    {
        return TopicBuilder.name(kafkaProperties.getOutputPaymentTopic()).partitions(kafkaProperties.getOutputPaymentTopicPartitions()).build();
    }
}
