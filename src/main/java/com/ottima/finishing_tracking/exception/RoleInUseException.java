package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class RoleInUseException extends RuntimeException {
    public RoleInUseException() {
        super(Messages.ROLE_IN_USE);
    }

    public RoleInUseException(String message) {
        super(message);
    }
}
