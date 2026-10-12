package com.ottima.finishing_tracking.project_chat.service;

import com.ottima.finishing_tracking.comment.entity.Comment;
import com.ottima.finishing_tracking.comment.repository.CommentRepository;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.daily_update.entity.DailyUpdate;
import com.ottima.finishing_tracking.exception.CommentNotFoundException;
import com.ottima.finishing_tracking.exception.ProjectAccessDeniedException;
import com.ottima.finishing_tracking.exception.ProjectNotFoundException;
import com.ottima.finishing_tracking.exception.UnauthorizedActionException;
import com.ottima.finishing_tracking.project.entity.Project;
import com.ottima.finishing_tracking.project.repository.ProjectRepository;
import com.ottima.finishing_tracking.project_chat.dto.request.ForwardClientCommentRequest;
import com.ottima.finishing_tracking.project_chat.dto.request.SendProjectChatMessageRequest;
import com.ottima.finishing_tracking.project_chat.dto.response.ProjectChatMessageResponse;
import com.ottima.finishing_tracking.project_chat.entity.ProjectChatMessage;
import com.ottima.finishing_tracking.project_chat.mapper.ProjectChatMapper;
import com.ottima.finishing_tracking.project_chat.repository.ProjectChatRepository;
import com.ottima.finishing_tracking.security.AuthenticatedUserService;
import com.ottima.finishing_tracking.user.entity.User;
import com.ottima.finishing_tracking.notification.event.ProjectChatMessageSentEvent;
import com.ottima.finishing_tracking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectChatService {

    private final ProjectChatRepository projectChatRepository;
    private final ProjectRepository projectRepository;
    private final CommentRepository commentRepository;
    private final ProjectChatMapper projectChatMapper;
    private final AuthenticatedUserService authenticatedUserService;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public ProjectChatMessageResponse sendMessage(UUID projectId, SendProjectChatMessageRequest request) {
        Project project = getProjectAndValidateAccess(projectId);
        User currentUser = authenticatedUserService.getCurrentUser();

        String senderRole = currentUser.getRole() != null ? currentUser.getRole().getRoleName() : "USER";
        String senderDisplayName = currentUser.getFullNameEn() != null ? currentUser.getFullNameEn() : currentUser.getUsername();

        String trimmedMessage = request.getMessage() != null && !request.getMessage().trim().isEmpty()
                ? request.getMessage().trim()
                : null;
        List<String> attachments = request.getAttachments() != null ? request.getAttachments() : new ArrayList<>();

        if (trimmedMessage == null && attachments.isEmpty()) {
            throw new IllegalArgumentException("Message text or at least one attachment is required");
        }

        ProjectChatMessage chatMessage = ProjectChatMessage.builder()
                .project(project)
                .sender(currentUser)
                .senderName(senderDisplayName)
                .senderRole(senderRole)
                .message(trimmedMessage)
                .attachments(attachments)
                .forwardedFromClient(false)
                .build();

        ProjectChatMessage saved = projectChatRepository.save(chatMessage);

        // In-app notification to all active admins and assigned engineers (excluding sender)
        Set<Long> recipientIds = new HashSet<>();
        List<Long> adminIds = userRepository.findActiveAdminUserIds();
        if (adminIds != null) {
            recipientIds.addAll(adminIds);
        }
        if (project.getEngineer() != null && project.getEngineer().getUserId() != null) {
            recipientIds.add(project.getEngineer().getUserId());
        }
        if (project.getEngineerIds() != null) {
            recipientIds.addAll(project.getEngineerIds());
        }
        recipientIds.remove(currentUser.getUserId());

        if (!recipientIds.isEmpty()) {
            String snippet;
            if (trimmedMessage != null && !trimmedMessage.isBlank()) {
                snippet = trimmedMessage.length() > 60 ? trimmedMessage.substring(0, 57) + "..." : trimmedMessage;
            } else {
                int count = attachments.size();
                snippet = count == 1 ? "Sent an attachment" : "Sent " + count + " attachments";
            }

            eventPublisher.publishEvent(ProjectChatMessageSentEvent.builder()
                    .projectId(project.getProjectId())
                    .senderId(currentUser.getUserId())
                    .senderName(senderDisplayName)
                    .senderRole(senderRole)
                    .projectNameEn(project.getNameEn())
                    .projectNameAr(project.getNameAr())
                    .messageSnippet(snippet)
                    .recipientIds(new ArrayList<>(recipientIds))
                    .build());
        }

        ProjectChatMessageResponse response = projectChatMapper.toResponse(saved);

        // Real-time broadcast to open chat rooms
        try {
            messagingTemplate.convertAndSend("/topic/projects/" + projectId + "/chat", response);
        } catch (Exception e) {
            log.error("Failed to broadcast real-time chat message to project {}", projectId, e);
        }

        return response;
    }

    @Transactional(readOnly = true)
    public Page<ProjectChatMessageResponse> getMessages(UUID projectId, Pageable pageable) {
        getProjectAndValidateAccess(projectId);
        return projectChatRepository.findByProject_ProjectIdOrderByCreatedAtDesc(projectId, pageable)
                .map(projectChatMapper::toResponse);
    }

    @Transactional
    public ProjectChatMessageResponse forwardClientComment(UUID projectId, ForwardClientCommentRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        User currentUser = authenticatedUserService.getCurrentUser();
        if (currentUser.getRole() == null || !"ADMIN".equals(currentUser.getRole().getRoleName())) {
            throw new UnauthorizedActionException("Only administrators can forward client comments to project chat");
        }

        Comment comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(CommentNotFoundException::new);

        DailyUpdate dailyUpdate = comment.getDailyUpdate();
        if (dailyUpdate == null || dailyUpdate.getProjectItem() == null ||
                !dailyUpdate.getProjectItem().getProject().getProjectId().equals(projectId)) {
            throw new IllegalArgumentException("The specified comment does not belong to this project");
        }

        StringBuilder content = new StringBuilder();
        if (request.getAdditionalNote() != null && !request.getAdditionalNote().isBlank()) {
            content.append("توجيه الإدارة: ").append(request.getAdditionalNote().trim()).append("\n\n");
        }
        content.append("💬 [ملاحظة محولة من العميل: ")
                .append(comment.getClient() != null ? comment.getClient().getUsername() : "العميل")
                .append(" بشأن التحديث: '")
                .append(dailyUpdate.getTitle())
                .append("']:\n")
                .append(comment.getClientComment());

        String senderDisplayName = currentUser.getFullNameEn() != null ? currentUser.getFullNameEn() : currentUser.getUsername();

        ProjectChatMessage forwardedMessage = ProjectChatMessage.builder()
                .project(project)
                .sender(currentUser)
                .senderName(senderDisplayName)
                .senderRole("ADMIN")
                .message(content.toString())
                .attachments(new ArrayList<>())
                .forwardedFromClient(true)
                .dailyUpdateId(dailyUpdate.getDailyUpdateId())
                .dailyUpdateTitle(dailyUpdate.getTitle())
                .clientCommentId(comment.getId())
                .clientName(comment.getClient() != null ? comment.getClient().getUsername() : null)
                .build();

        ProjectChatMessage saved = projectChatRepository.save(forwardedMessage);

        // In-app notification for forwarded client feedback
        Set<Long> recipientIds = new HashSet<>();
        List<Long> adminIds = userRepository.findActiveAdminUserIds();
        if (adminIds != null) {
            recipientIds.addAll(adminIds);
        }
        if (project.getEngineer() != null && project.getEngineer().getUserId() != null) {
            recipientIds.add(project.getEngineer().getUserId());
        }
        if (project.getEngineerIds() != null) {
            recipientIds.addAll(project.getEngineerIds());
        }
        recipientIds.remove(currentUser.getUserId());

        if (!recipientIds.isEmpty()) {
            String clientSnippet = comment.getClientComment() != null && comment.getClientComment().length() > 50
                    ? comment.getClientComment().substring(0, 47) + "..."
                    : comment.getClientComment();
            String snippet = "Forwarded client feedback: " + (clientSnippet != null ? clientSnippet : "");

            eventPublisher.publishEvent(ProjectChatMessageSentEvent.builder()
                    .projectId(project.getProjectId())
                    .senderId(currentUser.getUserId())
                    .senderName(senderDisplayName)
                    .senderRole("ADMIN")
                    .projectNameEn(project.getNameEn())
                    .projectNameAr(project.getNameAr())
                    .messageSnippet(snippet)
                    .recipientIds(new ArrayList<>(recipientIds))
                    .build());
        }

        ProjectChatMessageResponse response = projectChatMapper.toResponse(saved);

        // Real-time broadcast to open chat rooms
        try {
            messagingTemplate.convertAndSend("/topic/projects/" + projectId + "/chat", response);
        } catch (Exception e) {
            log.error("Failed to broadcast real-time forwarded comment to project {}", projectId, e);
        }

        return response;
    }

    private Project getProjectAndValidateAccess(UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        if (project.getDeletesAt() != null) {
            throw new ProjectNotFoundException();
        }

        User currentUser = authenticatedUserService.getCurrentUser();
        String role = currentUser.getRole() != null ? currentUser.getRole().getRoleName() : "";

        if ("ADMIN".equals(role)) {
            return project;
        }

        if ("ENGINEER".equals(role)) {
            if (!project.isAssignedToEngineer(currentUser.getUserId())) {
                throw new ProjectAccessDeniedException();
            }
            return project;
        }

        // Clients cannot access internal project chat
        throw new ProjectAccessDeniedException();
    }
}
