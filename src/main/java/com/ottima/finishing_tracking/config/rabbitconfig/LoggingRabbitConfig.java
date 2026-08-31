package com.ottima.finishing_tracking.config.rabbitconfig;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingRabbitConfig {

    @Bean
    public TopicExchange loggingExchange() {
        return new TopicExchange(RabbitConstants.LOGGING_EXCHANGE);
    }

    @Bean
    public DirectExchange loggingDlxExchange() {
        return new DirectExchange(RabbitConstants.LOGGING_DLX_EXCHANGE);
    }

    private Queue buildQueueWithDlx(String queueName) {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", RabbitConstants.LOGGING_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", queueName)
                .build();
    }

    @Bean
    public Queue activityLogQueue() {
        return buildQueueWithDlx(RabbitConstants.ACTIVITY_LOG_QUEUE);
    }

    @Bean
    public Queue activityLogDlq() {
        return QueueBuilder.durable(RabbitConstants.ACTIVITY_LOG_DLQ).build();
    }

    @Bean
    public Binding activityLogBinding(TopicExchange loggingExchange, Queue activityLogQueue) {
        return BindingBuilder.bind(activityLogQueue).to(loggingExchange).with(RabbitConstants.ACTIVITY_LOG_KEY);
    }

    @Bean
    public Binding activityLogDlqBinding(DirectExchange loggingDlxExchange, Queue activityLogDlq) {
        return BindingBuilder.bind(activityLogDlq).to(loggingDlxExchange).with(RabbitConstants.ACTIVITY_LOG_QUEUE);
    }
}