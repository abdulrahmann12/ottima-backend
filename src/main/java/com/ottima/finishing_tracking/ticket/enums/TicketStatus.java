package com.ottima.finishing_tracking.ticket.enums;

public enum TicketStatus {
    NEW,
    OPEN,
    RESOLVED,
    CLOSED,

    // Legacy statuses retained for backwards compatibility
    PENDING,
    VIEWED,
    APPROVED,
    REJECTED
}