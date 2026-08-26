package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class MailSendingException extends RuntimeException {

    public MailSendingException() {
        super(Messages.FAILED_EMAIL);
    }

    public MailSendingException(Throwable cause) {
        super(Messages.FAILED_EMAIL, cause);
    }
}