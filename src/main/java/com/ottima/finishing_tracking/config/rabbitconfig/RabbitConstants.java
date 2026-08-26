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

}