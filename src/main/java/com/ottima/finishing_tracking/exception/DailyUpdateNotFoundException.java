package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class DailyUpdateNotFoundException extends RuntimeException {
    public DailyUpdateNotFoundException() {
        super(Messages.DAILY_UPDATE_NOT_FOUND);
    }
}
