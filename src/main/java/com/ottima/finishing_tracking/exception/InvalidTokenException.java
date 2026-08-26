package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super(Messages.INVALID_TOKEN);
    }
}

