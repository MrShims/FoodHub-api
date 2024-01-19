package org.mrshim.orderservice.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.mrshim.orderservice.properties.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class TopicConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public NewTopic topic()
    {
        return TopicBuilder.name(kafkaProperties.getDeliveryTopic()).partitions(kafkaProperties.getDeliveryTopicPartitions()).build();
    }
}
