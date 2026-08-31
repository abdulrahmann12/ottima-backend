package com.ottima.finishing_tracking.ticket.service;

import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.Constants;
import com.ottima.finishing_tracking.exception.*;
import com.ottima.finishing_tracking.logging.annotation.LogActivity;
import com.ottima.finishing_tracking.logging.enums.ActionType;
import com.ottima.finishing_tracking.notification.event.TicketCreatedEvent;
import com.ottima.finishing_tracking.notification.event.TicketStatusChangedEvent;
import com.ottima.finishing_tracking.security.AuthenticatedUserService;
import com.ottima.finishing_tracking.project.entity.Project;
import com.ottima.finishing_tracking.project.repository.ProjectRepository;
import com.ottima.finishing_tracking.ticket.dto.request.CreateTicketRequest;
import com.ottima.finishing_tracking.ticket.dto.request.UpdateTicketRequest;
import com.ottima.finishing_tracking.ticket.dto.request.UpdateTicketStatusRequest;
import com.ottima.finishing_tracking.ticket.dto.response.TicketResponse;
import com.ottima.finishing_tracking.ticket.entity.InternalTicket;
import com.ottima.finishing_tracking.ticket.entity.TicketAttachment;
import com.ottima.finishing_tracking.ticket.enums.TicketStatus;
import com.ottima.finishing_tracking.ticket.mapper.TicketMapper;
import com.ottima.finishing_tracking.ticket.repository.InternalTicketRepository;
import com.ottima.finishing_tracking.user.entity.User;
import com.ottima.finishing_tracking.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class TicketService {

    private final InternalTicketRepository internalTicketRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;
    private final AuthenticatedUserService authenticatedUserService;
    private final ApplicationEventPublisher eventPublisher;

    // ==========================================
    // === Core Business Logic (Create, Update, Delete) ===
    // ==========================================

    @LogActivity(actionType = ActionType.CREATE, entityName = Constants.TICKET_ENTITY, details = Messages.TICKET_CREATED_LOG)
    @Transactional
    public TicketResponse createTicket(UUID projectId, @Valid CreateTicketRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(UserNotFoundException::new);

        User currentSender = authenticatedUserService.getCurrentUser();

        if (currentSender.getRole().getRoleName().contains("CLIENT")) {
            throw new UnauthorizedActionException(Messages.TICKET_CLIENT_SEND_DENIED);
        }

        if (receiver.getRole().getRoleName().contains("CLIENT")) {
            throw new UnauthorizedActionException(Messages.TICKET_CLIENT_RECEIVE_DENIED);
        }

        InternalTicket ticket = ticketMapper.toEntity(request);
        ticket.setProject(project);
        ticket.setSender(currentSender);
        ticket.setReceiver(receiver);
        ticket.setStatus(TicketStatus.PENDING);

        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            List<TicketAttachment> attachments = request.getAttachments().stream()
                    .map(ticketMapper::toAttachmentEntity)
                    .peek(attachment -> attachment.setTicket(ticket))
                    .collect(Collectors.toList());
            ticket.getAttachments().addAll(attachments);
        }

        InternalTicket savedTicket = internalTicketRepository.save(ticket);

        eventPublisher.publishEvent(TicketCreatedEvent.builder()
                .ticketId(savedTicket.getTicketId())
                .receiverId(savedTicket.getReceiver().getUserId())
                .senderRole(currentSender.getRole().getRoleName())
                .senderNameAR(currentSender.getFullNameAr())
                .senderNameEn(currentSender.getFullNameEn())
                .ticketTitle(savedTicket.getTitle())
                .build());

        return ticketMapper.toResponse(savedTicket);
    }

    @LogActivity(actionType = ActionType.UPDATE, entityName = Constants.TICKET_ENTITY, details = Messages.TICKET_UPDATED_LOG)
    @Transactional
    public TicketResponse updateTicket(UUID ticketId, @Valid UpdateTicketRequest request) {
        InternalTicket ticket = internalTicketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);

        User currentUser = authenticatedUserService.getCurrentUser();

        if ("CLIENT".equals(currentUser.getRole().getRoleName())) {
            throw new UnauthorizedActionException(Messages.TICKET_CLIENT_SEND_DENIED);
        }

        if ("CLIENT".equals(ticket.getReceiver().getRole().getRoleName())){
            throw new UnauthorizedActionException(Messages.TICKET_CLIENT_RECEIVE_DENIED);
        }

        if (!ticket.getSender().getUserId().equals(currentUser.getUserId())) {
            throw new UnauthorizedActionException(Messages.TICKET_UPDATE_DENIED);
        }

        if (ticket.getStatus() != TicketStatus.PENDING) {
            throw new TicketAlreadyProcessedException();
        }

        ticketMapper.updateEntityFromRequest(request, ticket);

        if (request.getAttachments() != null) {
            ticket.getAttachments().clear();
            List<TicketAttachment> newAttachments = request.getAttachments().stream()
                    .map(ticketMapper::toAttachmentEntity)
                    .peek(attachment -> attachment.setTicket(ticket))
                    .collect(Collectors.toList());
            ticket.getAttachments().addAll(newAttachments);
        }

        InternalTicket updatedTicket = internalTicketRepository.save(ticket);
        return ticketMapper.toResponse(updatedTicket);
    }

    @LogActivity(actionType = ActionType.UPDATE, entityName = Constants.TICKET_ENTITY, details = Messages.TICKET_STATUS_UPDATED_LOG)
    @Transactional
    public TicketResponse updateTicketStatus(UUID ticketId, UpdateTicketStatusRequest request) {
        InternalTicket ticket = internalTicketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);

        User currentUser = authenticatedUserService.getCurrentUser();

        boolean isAdmin = "ADMIN".equals(currentUser.getRole().getRoleName());
        boolean isReceiver = ticket.getReceiver().getUserId().equals(currentUser.getUserId());

        if (!isAdmin && !isReceiver) {
            throw new UnauthorizedActionException(Messages.TICKET_STATUS_UPDATE_DENIED);
        }

        ticket.setStatus(request.getStatus());
        InternalTicket updatedTicket = internalTicketRepository.save(ticket);

        eventPublisher.publishEvent(TicketStatusChangedEvent.builder()
                .ticketId(updatedTicket.getTicketId())
                .engineerId(updatedTicket.getSender().getUserId())
                .ticketTitle(updatedTicket.getTitle())
                .newStatus(updatedTicket.getStatus().name())
                .build());

        return ticketMapper.toResponse(updatedTicket);
    }

    @LogActivity(actionType = ActionType.DELETE, entityName = Constants.TICKET_ENTITY, details = Messages.TICKET_DELETED_LOG)
    @Transactional
    public void deleteTicket(UUID ticketId) {
        InternalTicket ticket = internalTicketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);

        User currentUser = authenticatedUserService.getCurrentUser();

        if (!ticket.getSender().getUserId().equals(currentUser.getUserId())) {
            throw new UnauthorizedActionException(Messages.TICKET_DELETE_DENIED);
        }
        if (ticket.getStatus() != TicketStatus.PENDING) {
            throw new TicketAlreadyProcessedException();
        }

        internalTicketRepository.delete(ticket);
    }


    public TicketResponse getTicketById(UUID ticketId) {
        InternalTicket ticket = internalTicketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);
        return ticketMapper.toResponse(ticket);
    }

    public Page<TicketResponse> getTicketsByProject(UUID projectId, Pageable pageable) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException();
        }
        return internalTicketRepository.findByProject_ProjectIdOrderByCreatedAtDesc(projectId, pageable)
                .map(ticketMapper::toResponse);
    }

    public Page<TicketResponse> getMyInbox(Pageable pageable) {
        User currentUser = authenticatedUserService.getCurrentUser();
        return internalTicketRepository.findByReceiver_UserIdOrderByCreatedAtDesc(currentUser.getUserId(), pageable)
                .map(ticketMapper::toResponse);
    }

    public Page<TicketResponse> getMySentRequests(Pageable pageable) {
        User currentUser = authenticatedUserService.getCurrentUser();
        return internalTicketRepository.findBySender_UserIdOrderByCreatedAtDesc(currentUser.getUserId(), pageable)
                .map(ticketMapper::toResponse);
    }

    public Page<TicketResponse> getAllTicketsForSpecificUser(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
        return internalTicketRepository
                .findBySender_UserIdOrReceiver_UserIdOrderByCreatedAtDesc(userId, userId, pageable)
                .map(ticketMapper::toResponse);
    }
}