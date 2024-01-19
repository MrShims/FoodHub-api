package org.mrshim.transactionalservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "kafka")
@Component
@Data
public class KafkaProperties {
    private String bootstrapServers;
    private String groupId;
    private String schemaRegistryUrl;
    private String outputPaymentTopic;
    private Integer outputPaymentTopicPartitions;
    private String outputNotificationTopic;
    private Integer outputNotificationTopicPartitions;
}
