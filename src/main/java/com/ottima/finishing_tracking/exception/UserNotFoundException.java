package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super(Messages.USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}

