package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class ImageDeletedException extends RuntimeException {

    public ImageDeletedException() {
        super(Messages.IMAGE_DELETED_FAILED);
    }
}

