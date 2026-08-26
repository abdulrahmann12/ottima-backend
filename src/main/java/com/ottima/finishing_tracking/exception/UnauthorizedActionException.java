package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class UnauthorizedActionException extends RuntimeException{

    public UnauthorizedActionException(String message) {
        super(message);
    }
}