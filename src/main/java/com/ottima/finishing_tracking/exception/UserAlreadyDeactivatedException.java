package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class UserAlreadyDeactivatedException extends RuntimeException {
    public UserAlreadyDeactivatedException() {
        super(Messages.USER_ALREADY_DEACTIVATED);
    }
}

