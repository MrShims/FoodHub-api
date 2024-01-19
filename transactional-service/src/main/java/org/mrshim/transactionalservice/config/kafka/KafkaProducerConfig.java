package org.mrshim.transactionalservice.config.kafka;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.mrshim.transactionalservice.properties.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, org.mrshim.foodhub.PaymentNotificationMessage> producerFactory1() {
        Map<String, Object> configProps = KafkaConfigProp();

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ProducerFactory<String, org.mrshim.foodhub.PaymentEvent> producerFactory2() {
        Map<String, Object> configProps = KafkaConfigProp();

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private Map<String, Object> KafkaConfigProp() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        configProps.put("schema.registry.url", kafkaProperties.getSchemaRegistryUrl());
        return configProps;
    }

    @Bean
    public KafkaTemplate<String, org.mrshim.foodhub.PaymentNotificationMessage> kafkaPaymentNotificationTemplate() {
        return new KafkaTemplate<>(producerFactory1());
    }
    @Bean
    public KafkaTemplate<String, org.mrshim.foodhub.PaymentEvent> kafkaPaymentEventTemplate() {
        return new KafkaTemplate<>(producerFactory2());
    }
}
