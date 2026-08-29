package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class UnapprovedDailyUpdateCommentException extends RuntimeException {
    public UnapprovedDailyUpdateCommentException() {
        super(Messages.CANNOT_COMMENT_ON_UNAPPROVED_UPDATE);
    }
}
