package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class StandardItemInUseException extends RuntimeException {
    public StandardItemInUseException() {
        super(Messages.STANDARD_ITEM_IN_USE);
    }
}
