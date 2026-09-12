package com.ottima.finishing_tracking.config.rabbitconfig;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationRabbitConfig {

    // ── Main Exchange ──────────────────────────────────────────────────────

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(RabbitConstants.NOTIFICATION_EXCHANGE);
    }

    // ── Dead-Letter Exchange ───────────────────────────────────────────────

    @Bean
    public DirectExchange notificationDlxExchange() {
        return new DirectExchange(RabbitConstants.NOTIFICATION_DLX_EXCHANGE);
    }

    // ── Helper: build a queue with DLX arguments ──────────────────────────

    private Queue buildQueueWithDlx(String queueName) {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", RabbitConstants.NOTIFICATION_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", queueName)
                .build();
    }

    // ── Main Queues (all configured with DLX) ─────────────────────────────

    @Bean
    public Queue dailyUpdateCreatedQueue() {
        return buildQueueWithDlx(RabbitConstants.DAILY_UPDATE_CREATED_QUEUE);
    }

    @Bean
    public Queue dailyUpdateEvaluatedQueue() {
        return buildQueueWithDlx(RabbitConstants.DAILY_UPDATE_EVALUATED_QUEUE);
    }

    @Bean
    public Queue commentAddedQueue() {
        return buildQueueWithDlx(RabbitConstants.COMMENT_ADDED_QUEUE);
    }

    @Bean
    public Queue commentRepliedQueue() {
        return buildQueueWithDlx(RabbitConstants.COMMENT_REPLIED_QUEUE);
    }

    @Bean
    public Queue ticketCreatedQueue() {
        return buildQueueWithDlx(RabbitConstants.TICKET_CREATED_QUEUE);
    }

    // ── Dead-Letter Queues ────────────────────────────────────────────────

    @Bean
    public Queue dailyUpdateCreatedDlq() {
        return QueueBuilder.durable(RabbitConstants.DAILY_UPDATE_CREATED_DLQ).build();
    }

    @Bean
    public Queue dailyUpdateEvaluatedDlq() {
        return QueueBuilder.durable(RabbitConstants.DAILY_UPDATE_EVALUATED_DLQ).build();
    }

    @Bean
    public Queue commentAddedDlq() {
        return QueueBuilder.durable(RabbitConstants.COMMENT_ADDED_DLQ).build();
    }

    @Bean
    public Queue commentRepliedDlq() {
        return QueueBuilder.durable(RabbitConstants.COMMENT_REPLIED_DLQ).build();
    }

    @Bean
    public Queue ticketCreatedDlq() {
        return QueueBuilder.durable(RabbitConstants.TICKET_CREATED_DLQ).build();
    }

    // ── Main Queue Bindings ───────────────────────────────────────────────

    @Bean
    public Binding dailyUpdateCreatedBinding(TopicExchange notificationExchange, Queue dailyUpdateCreatedQueue) {
        return BindingBuilder.bind(dailyUpdateCreatedQueue).to(notificationExchange).with(RabbitConstants.DAILY_UPDATE_CREATED_KEY);
    }

    @Bean
    public Binding dailyUpdateEvaluatedBinding(TopicExchange notificationExchange, Queue dailyUpdateEvaluatedQueue) {
        return BindingBuilder.bind(dailyUpdateEvaluatedQueue).to(notificationExchange).with(RabbitConstants.DAILY_UPDATE_EVALUATED_KEY);
    }

    @Bean
    public Binding commentAddedBinding(TopicExchange notificationExchange, Queue commentAddedQueue) {
        return BindingBuilder.bind(commentAddedQueue).to(notificationExchange).with(RabbitConstants.COMMENT_ADDED_KEY);
    }

    @Bean
    public Binding commentRepliedBinding(TopicExchange notificationExchange, Queue commentRepliedQueue) {
        return BindingBuilder.bind(commentRepliedQueue).to(notificationExchange).with(RabbitConstants.COMMENT_REPLIED_KEY);
    }

    @Bean
    public Binding ticketCreatedBinding(TopicExchange notificationExchange, Queue ticketCreatedQueue) {
        return BindingBuilder.bind(ticketCreatedQueue).to(notificationExchange).with(RabbitConstants.TICKET_CREATED_KEY);
    }

    // ── Dead-Letter Queue Bindings ────────────────────────────────────────

    @Bean
    public Binding dailyUpdateCreatedDlqBinding(DirectExchange notificationDlxExchange, Queue dailyUpdateCreatedDlq) {
        return BindingBuilder.bind(dailyUpdateCreatedDlq).to(notificationDlxExchange).with(RabbitConstants.DAILY_UPDATE_CREATED_QUEUE);
    }

    @Bean
    public Binding dailyUpdateEvaluatedDlqBinding(DirectExchange notificationDlxExchange, Queue dailyUpdateEvaluatedDlq) {
        return BindingBuilder.bind(dailyUpdateEvaluatedDlq).to(notificationDlxExchange).with(RabbitConstants.DAILY_UPDATE_EVALUATED_QUEUE);
    }

    @Bean
    public Binding commentAddedDlqBinding(DirectExchange notificationDlxExchange, Queue commentAddedDlq) {
        return BindingBuilder.bind(commentAddedDlq).to(notificationDlxExchange).with(RabbitConstants.COMMENT_ADDED_QUEUE);
    }

    @Bean
    public Binding commentRepliedDlqBinding(DirectExchange notificationDlxExchange, Queue commentRepliedDlq) {
        return BindingBuilder.bind(commentRepliedDlq).to(notificationDlxExchange).with(RabbitConstants.COMMENT_REPLIED_QUEUE);
    }

    @Bean
    public Binding ticketCreatedDlqBinding(DirectExchange notificationDlxExchange, Queue ticketCreatedDlq) {
        return BindingBuilder.bind(ticketCreatedDlq).to(notificationDlxExchange).with(RabbitConstants.TICKET_CREATED_QUEUE);
    }
}
