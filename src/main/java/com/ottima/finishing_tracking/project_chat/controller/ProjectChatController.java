package com.ottima.finishing_tracking.project_chat.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.project_chat.dto.request.ForwardClientCommentRequest;
import com.ottima.finishing_tracking.project_chat.dto.request.SendProjectChatMessageRequest;
import com.ottima.finishing_tracking.project_chat.service.ProjectChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/chat")
@RequiredArgsConstructor
@Tag(name = "Project Chat", description = "Global internal chat for project between admin and engineers")
public class ProjectChatController {

    private final ProjectChatService projectChatService;

    @Operation(summary = "Get project chat messages", description = "Retrieve paginated messages for this project's global chat")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
    public ResponseEntity<BaseResponse> getMessages(
            @PathVariable UUID projectId,
            Pageable pageable) {
        return ResponseEntity.ok(
                new BaseResponse("Messages fetched successfully", projectChatService.getMessages(projectId, pageable))
        );
    }

    @Operation(summary = "Send a message in project chat", description = "Send a text message and optional attachments to project chat")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
    public ResponseEntity<BaseResponse> sendMessage(
            @PathVariable UUID projectId,
            @Valid @RequestBody SendProjectChatMessageRequest request) {
        return ResponseEntity.ok(
                new BaseResponse("Message sent successfully", projectChatService.sendMessage(projectId, request))
        );
    }

    @Operation(summary = "Forward a client comment to project chat", description = "Admin forwards a client comment on a daily update to the engineer chat thread")
    @PostMapping("/forward-client-comment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> forwardClientComment(
            @PathVariable UUID projectId,
            @Valid @RequestBody ForwardClientCommentRequest request) {
        return ResponseEntity.ok(
                new BaseResponse("Client comment forwarded successfully", projectChatService.forwardClientComment(projectId, request))
        );
    }
}
