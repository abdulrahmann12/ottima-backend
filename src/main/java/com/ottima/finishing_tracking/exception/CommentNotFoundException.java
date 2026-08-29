package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException() {
        super(Messages.COMMENT_NOT_FOUND);
    }
}
