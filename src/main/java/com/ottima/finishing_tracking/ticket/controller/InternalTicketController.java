package com.ottima.finishing_tracking.ticket.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
import com.ottima.finishing_tracking.ticket.dto.request.CreateTicketRequest;
import com.ottima.finishing_tracking.ticket.dto.request.UpdateTicketRequest;
import com.ottima.finishing_tracking.ticket.dto.request.UpdateTicketStatusRequest;
import com.ottima.finishing_tracking.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/internal-tickets")
@RequiredArgsConstructor
@Tag(name = SwaggerMessages.TAG_TICKET, description = SwaggerMessages.TAG_TICKET_DESC)
@PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
public class InternalTicketController {

    private final TicketService ticketService;

    @Operation(summary = SwaggerMessages.CREATE_TICKET, description = SwaggerMessages.CREATE_TICKET_DESC)
    @PostMapping("/projects/{projectId}")
    public ResponseEntity<BaseResponse> createTicket(
            @PathVariable UUID projectId,
            @Valid @RequestBody CreateTicketRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new BaseResponse(Messages.TICKET_CREATED, ticketService.createTicket(projectId, request))
        );
    }

    @Operation(summary = SwaggerMessages.UPDATE_TICKET, description = SwaggerMessages.UPDATE_TICKET_DESC)
    @PutMapping("/{ticketId}")
    public ResponseEntity<BaseResponse> updateTicket(
            @PathVariable UUID ticketId,
            @Valid @RequestBody UpdateTicketRequest request) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.TICKET_UPDATED, ticketService.updateTicket(ticketId, request))
        );
    }

    @Operation(summary = SwaggerMessages.UPDATE_TICKET_STATUS, description = SwaggerMessages.UPDATE_TICKET_STATUS_DESC)
    @PatchMapping("/{ticketId}/status")
    public ResponseEntity<BaseResponse> updateTicketStatus(
            @PathVariable UUID ticketId,
            @Valid @RequestBody UpdateTicketStatusRequest request) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.TICKET_STATUS_UPDATED, ticketService.updateTicketStatus(ticketId, request))
        );
    }

    @Operation(summary = SwaggerMessages.DELETE_TICKET, description = SwaggerMessages.DELETE_TICKET_DESC)
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<BaseResponse> deleteTicket(
            @PathVariable UUID ticketId) {

        ticketService.deleteTicket(ticketId);
        return ResponseEntity.ok(
                new BaseResponse(Messages.TICKET_DELETED, null)
        );
    }

    @Operation(summary = SwaggerMessages.GET_TICKET_BY_ID, description = SwaggerMessages.GET_TICKET_BY_ID_DESC)
    @GetMapping("/{ticketId}")
    public ResponseEntity<BaseResponse> getTicketById(
            @PathVariable UUID ticketId) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.TICKET_FETCHED, ticketService.getTicketById(ticketId))
        );
    }

    @Operation(summary = SwaggerMessages.GET_TICKETS_BY_PROJECT, description = SwaggerMessages.GET_TICKETS_BY_PROJECT_DESC)
    @GetMapping("/projects/{projectId}")
    public ResponseEntity<BaseResponse> getTicketsByProject(
            @PathVariable UUID projectId,
            Pageable pageable) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.TICKETS_FETCHED, ticketService.getTicketsByProject(projectId, pageable))
        );
    }

    @Operation(summary = SwaggerMessages.GET_MY_INBOX, description = SwaggerMessages.GET_MY_INBOX_DESC)
    @GetMapping("/my-inbox")
    public ResponseEntity<BaseResponse> getMyInbox(Pageable pageable) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.TICKET_INBOX_FETCHED, ticketService.getMyInbox(pageable))
        );
    }

    @Operation(summary = SwaggerMessages.GET_MY_SENT, description = SwaggerMessages.GET_MY_SENT_DESC)
    @GetMapping("/my-sent")
    public ResponseEntity<BaseResponse> getMySentRequests(Pageable pageable) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.TICKET_SENT_FETCHED, ticketService.getMySentRequests(pageable))
        );
    }

    @Operation(summary = SwaggerMessages.GET_TICKETS_BY_USER, description = SwaggerMessages.GET_TICKETS_BY_USER_DESC)
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> getAllTicketsForSpecificUser(
            @PathVariable Long userId,
            Pageable pageable) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.TICKET_USER_FETCHED, ticketService.getAllTicketsForSpecificUser(userId, pageable))
        );
    }
}