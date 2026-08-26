package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class ImageNullException extends RuntimeException {
    public ImageNullException() {
        super(Messages.IMAGE_NULL);
    }
}

