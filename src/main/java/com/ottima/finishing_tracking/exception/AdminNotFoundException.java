package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class AdminNotFoundException extends RuntimeException {

    public AdminNotFoundException() {
        super(Messages.ADMIN_NOT_FOUND);
    }
}
