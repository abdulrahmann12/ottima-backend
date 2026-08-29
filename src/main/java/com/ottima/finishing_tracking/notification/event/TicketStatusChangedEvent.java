package com.ottima.finishing_tracking.notification.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class TicketStatusChangedEvent {
    private UUID ticketId;
    private Long engineerId;
    private String ticketTitle;
    private String newStatus;
}