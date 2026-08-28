package com.ottima.finishing_tracking.exception;
import com.ottima.finishing_tracking.common.messages.Messages;

public class WeightLimitExceededException extends RuntimeException {
    public WeightLimitExceededException() {
        super(Messages.WEIGHT_LIMIT_EXCEEDED);
    }
}