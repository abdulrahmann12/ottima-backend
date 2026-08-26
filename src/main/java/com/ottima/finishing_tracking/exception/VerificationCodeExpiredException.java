package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class VerificationCodeExpiredException extends RuntimeException{
    public VerificationCodeExpiredException() {
        super(Messages.VERIFICATION_CODE_EXPIRED);
    }
}
