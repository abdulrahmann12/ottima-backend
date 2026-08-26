package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class InvalidNewPasswordException extends RuntimeException {
    public InvalidNewPasswordException() {
        super(Messages.INVALID_NEW_PASSWORD);
    }
}

