package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class FinancialRecordNotFoundException extends RuntimeException {
    public FinancialRecordNotFoundException() {
        super("Financial record not found");
    }
}
