package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class WrongPasswordException extends RuntimeException {

    public WrongPasswordException() {
        super(Messages.WRONG_PASSWORD);
    }
}

