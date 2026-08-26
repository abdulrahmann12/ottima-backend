package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class UserNotActiveException extends RuntimeException {

    public UserNotActiveException() {
        super(Messages.USER_NOT_ACTIVE);
    }
}

