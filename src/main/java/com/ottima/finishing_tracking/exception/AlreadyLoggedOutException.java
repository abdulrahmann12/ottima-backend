package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class AlreadyLoggedOutException extends RuntimeException {
    public AlreadyLoggedOutException() {
        super(Messages.ALREADY_LOGGED_OUT);
    }
}

