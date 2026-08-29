package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class CommentAlreadyRepliedException extends RuntimeException {

    public CommentAlreadyRepliedException() {
        super(Messages.CANNOT_DELETE_COMMENT);
    }
}
