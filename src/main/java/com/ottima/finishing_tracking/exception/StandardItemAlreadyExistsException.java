package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class StandardItemAlreadyExistsException extends RuntimeException {

    public StandardItemAlreadyExistsException() {
        super(Messages.STANDARD_ITEM_ALREADY_EXISTS);
    }
}
