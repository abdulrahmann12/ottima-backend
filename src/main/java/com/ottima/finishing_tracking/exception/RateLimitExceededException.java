package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class RateLimitExceededException extends RuntimeException{
    public RateLimitExceededException(){
        super(Messages.TOO_MANY_REQUESTS);
    }
}
