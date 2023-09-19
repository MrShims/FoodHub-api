package org.mrshim.deliveryservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Value("${rabbitmq.queue.name}")
    private String name;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Bean
    public Queue queue()
    {
        return new Queue(name);
    }

    @Bean
    public TopicExchange topicExchange()
    {
        return new TopicExchange(exchange);
    }


    @Bean
    public Binding binding()
    {
        return BindingBuilder.bind(queue())
                .to(topicExchange())
                .with(routingKey);
    }
}
