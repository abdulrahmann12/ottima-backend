package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException() {
        super("Notification not found.");
    }
}
