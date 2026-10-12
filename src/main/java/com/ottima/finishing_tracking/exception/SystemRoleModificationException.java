package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class SystemRoleModificationException extends RuntimeException {
    public SystemRoleModificationException() {
        super(Messages.SYSTEM_ROLE_CANNOT_BE_MODIFIED);
    }

    public SystemRoleModificationException(String message) {
        super(message);
    }
}
