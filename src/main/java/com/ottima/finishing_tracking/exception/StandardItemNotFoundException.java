package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class StandardItemNotFoundException extends RuntimeException {

    public StandardItemNotFoundException() {
        super(Messages.STANDARD_ITEM_NOT_FOUND);
    }
}
