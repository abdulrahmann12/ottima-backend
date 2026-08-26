package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class VerificationCodeAlreadySentException extends RuntimeException {
    public VerificationCodeAlreadySentException() {
        super(Messages.VERIFICATION_CODE_ALREADY_SENT);
    }
}
