package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super(Messages.EMAIL_ALREADY_EXISTS);
    }
}

