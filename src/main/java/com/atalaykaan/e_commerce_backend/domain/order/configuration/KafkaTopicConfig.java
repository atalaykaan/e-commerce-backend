package com.atalaykaan.e_commerce_backend.domain.order.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic.order-created}")
    private String orderCreatedTopic;

    @Value("${spring.kafka.topic.order-updated}")
    private String orderUpdatedTopic;

    @Bean
    public NewTopic orderCreatedTopic() {

        return TopicBuilder
                .name(orderCreatedTopic)
                .build();
    }

    @Bean
    public NewTopic orderUpdatedTopic() {

        return TopicBuilder
                .name(orderUpdatedTopic)
                .build();
    }
}
