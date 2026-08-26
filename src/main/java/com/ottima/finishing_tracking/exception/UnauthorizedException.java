package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException() {
        super(Messages.UNAUTHORIZED);
    }
}