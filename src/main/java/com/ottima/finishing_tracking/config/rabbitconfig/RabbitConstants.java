package com.ottima.finishing_tracking.config.rabbitconfig;

public class RabbitConstants {

    // Exchange
    public static final String AUTH_EXCHANGE = "auth.exchange";

    // Dead-Letter Exchange
    public static final String AUTH_DLX_EXCHANGE = "auth.dlx.exchange";

    // Queues
    public static final String USER_EMAIL_CHANGE_QUEUE = "auth.user.email.change.queue";
    public static final String PASSWORD_RESET_QUEUE = "auth.password.reset.queue";
    public static final String CODE_REGENERATED_QUEUE = "auth.code.regenerated.queue";

    // Dead-Letter Queues
    public static final String USER_EMAIL_CHANGE_DLQ = "auth.user.email.change.queue.dlq";
    public static final String PASSWORD_RESET_DLQ = "auth.password.reset.queue.dlq";
    public static final String CODE_REGENERATED_DLQ = "auth.code.regenerated.queue.dlq";

    // Routing Keys
    public static final String USER_EMAIL_CHANGE_KEY = "auth.user.email.change";
    public static final String PASSWORD_RESET_KEY = "auth.password.reset";
    public static final String CODE_REGENERATED_KEY = "auth.code.regenerated";


    // Logging Exchange
    public static final String LOGGING_EXCHANGE = "logging.exchange";
    public static final String LOGGING_DLX_EXCHANGE = "logging.dlx.exchange";

    // Logging Queues
    public static final String ACTIVITY_LOG_QUEUE = "logging.activity.log.queue";
    public static final String ACTIVITY_LOG_DLQ = "logging.activity.log.queue.dlq";

    // Logging Routing Keys
    public static final String ACTIVITY_LOG_KEY = "logging.activity.log";


    // Notification Email Exchange
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String NOTIFICATION_DLX_EXCHANGE = "notification.dlx.exchange";

    // Notification Email Queues
    public static final String DAILY_UPDATE_CREATED_QUEUE = "notification.daily.update.created.queue";
    public static final String DAILY_UPDATE_EVALUATED_QUEUE = "notification.daily.update.evaluated.queue";
    public static final String COMMENT_ADDED_QUEUE = "notification.comment.added.queue";
    public static final String COMMENT_REPLIED_QUEUE = "notification.comment.replied.queue";
    public static final String TICKET_CREATED_QUEUE = "notification.ticket.created.queue";

    // Notification Email Dead-Letter Queues
    public static final String DAILY_UPDATE_CREATED_DLQ = "notification.daily.update.created.queue.dlq";
    public static final String DAILY_UPDATE_EVALUATED_DLQ = "notification.daily.update.evaluated.queue.dlq";
    public static final String COMMENT_ADDED_DLQ = "notification.comment.added.queue.dlq";
    public static final String COMMENT_REPLIED_DLQ = "notification.comment.replied.queue.dlq";
    public static final String TICKET_CREATED_DLQ = "notification.ticket.created.queue.dlq";

    // Notification Email Routing Keys
    public static final String DAILY_UPDATE_CREATED_KEY = "notification.daily.update.created";
    public static final String DAILY_UPDATE_EVALUATED_KEY = "notification.daily.update.evaluated";
    public static final String COMMENT_ADDED_KEY = "notification.comment.added";
    public static final String COMMENT_REPLIED_KEY = "notification.comment.replied";
    public static final String TICKET_CREATED_KEY = "notification.ticket.created";
}