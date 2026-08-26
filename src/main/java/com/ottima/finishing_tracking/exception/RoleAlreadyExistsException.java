package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class RoleAlreadyExistsException extends RuntimeException {

    public RoleAlreadyExistsException() {
        super(Messages.ROLE_ALREADY_EXISTS);
    }
}

