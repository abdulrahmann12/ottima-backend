package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class UserAlreadyActivatedException extends RuntimeException {
    public UserAlreadyActivatedException() {
        super(Messages.USER_ALREADY_ACTIVATED);
    }
}

