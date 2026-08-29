package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class TicketAlreadyProcessedException extends RuntimeException {
    public TicketAlreadyProcessedException() {
        super(Messages.TICKET_ALREADY_PROCESSED);
    }
}
