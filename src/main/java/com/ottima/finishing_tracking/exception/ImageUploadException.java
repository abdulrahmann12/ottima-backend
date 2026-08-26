package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class ImageUploadException extends RuntimeException {

    public ImageUploadException() {
        super(Messages.IMAGE_UPLOAD_FAILED);
    }
}

