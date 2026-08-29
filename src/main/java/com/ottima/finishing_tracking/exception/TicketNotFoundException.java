package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException() {
        super(Messages.TICKET_NOT_FOUND);
    }
}
