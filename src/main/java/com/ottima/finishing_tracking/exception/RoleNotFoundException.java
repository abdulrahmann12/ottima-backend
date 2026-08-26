package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException() {
        super(Messages.ROLE_NOT_FOUND);
    }
}

