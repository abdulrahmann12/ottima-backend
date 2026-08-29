package com.ottima.finishing_tracking.comment.controller;

import com.ottima.finishing_tracking.comment.dto.request.AddCommentRequest;
import com.ottima.finishing_tracking.comment.dto.request.EditCommentRequest;
import com.ottima.finishing_tracking.comment.service.CommentService;
import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
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
@RequestMapping("/api/v1/client/daily-updates/{dailyUpdateId}/comments")
@RequiredArgsConstructor
@Tag(name = SwaggerMessages.TAG_COMMENT_CLIENT, description = SwaggerMessages.TAG_COMMENT_CLIENT_DESC)
@PreAuthorize("hasRole('CLIENT')")
public class ClientCommentController {

    private final CommentService commentService;

    @Operation(summary = SwaggerMessages.ADD_COMMENT, description = SwaggerMessages.ADD_COMMENT_DESC)
    @PostMapping
    public ResponseEntity<BaseResponse> addComment(
            @PathVariable UUID dailyUpdateId,
            @Valid @RequestBody AddCommentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new BaseResponse(Messages.COMMENT_ADDED, commentService.addComment(dailyUpdateId, request))
        );
    }

    @Operation(summary = SwaggerMessages.GET_COMMENTS_CLIENT, description = SwaggerMessages.GET_COMMENTS_CLIENT_DESC)
    @GetMapping
    public ResponseEntity<BaseResponse> getComments(
            @PathVariable UUID dailyUpdateId,
            Pageable pageable) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.COMMENTS_FETCHED, commentService.getCommentsForUpdate(dailyUpdateId, pageable))
        );
    }

    @Operation(summary = SwaggerMessages.EDIT_COMMENT, description = SwaggerMessages.EDIT_COMMENT_DESC)
    @PutMapping("/{commentId}")
    public ResponseEntity<BaseResponse> editComment(
            @PathVariable UUID dailyUpdateId,
            @PathVariable UUID commentId,
            @Valid @RequestBody EditCommentRequest request) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.COMMENT_UPDATED, commentService.editComment(commentId, request))
        );
    }

    @Operation(summary = SwaggerMessages.DELETE_COMMENT_CLIENT, description = SwaggerMessages.DELETE_COMMENT_CLIENT_DESC)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<BaseResponse> deleteClientComment(
            @PathVariable UUID dailyUpdateId,
            @PathVariable UUID commentId) {

        commentService.deleteComment(commentId, false);

        return ResponseEntity.ok(
                new BaseResponse(Messages.COMMENT_DELETED, null)
        );
    }
}