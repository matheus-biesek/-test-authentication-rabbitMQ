package com.stock.Inventory.rabbitMQ.conf;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue validationQueue() {
        return new Queue("token.validation", true);
    }
}
