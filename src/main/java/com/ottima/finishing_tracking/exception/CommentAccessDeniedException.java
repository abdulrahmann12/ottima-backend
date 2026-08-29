package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class CommentAccessDeniedException extends RuntimeException {
    public CommentAccessDeniedException() {
        super(Messages.COMMENT_ACCESS_DENIED);
    }
}