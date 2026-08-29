package com.ottima.finishing_tracking.comment.controller;

import com.ottima.finishing_tracking.comment.dto.request.ReplyCommentRequest;
import com.ottima.finishing_tracking.comment.service.CommentService;
import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
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
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = SwaggerMessages.TAG_COMMENT_ADMIN, description = SwaggerMessages.TAG_COMMENT_ADMIN_DESC)
@PreAuthorize("hasRole('ADMIN')")
public class AdminCommentController {

    private final CommentService commentService;

    @Operation(summary = SwaggerMessages.REPLY_TO_COMMENT, description = SwaggerMessages.REPLY_TO_COMMENT_DESC)
    @PutMapping("/comments/{commentId}/reply")
    public ResponseEntity<BaseResponse> replyToComment(
            @PathVariable UUID commentId,
            @Valid @RequestBody ReplyCommentRequest request) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.COMMENT_REPLIED, commentService.replyToComment(commentId, request))
        );
    }

    @Operation(summary = SwaggerMessages.GET_UPDATE_COMMENTS_ADMIN, description = SwaggerMessages.GET_UPDATE_COMMENTS_ADMIN_DESC)
    @GetMapping("/daily-updates/{dailyUpdateId}/comments")
    public ResponseEntity<BaseResponse> getCommentsForUpdate(
            @PathVariable UUID dailyUpdateId,
            Pageable pageable) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.COMMENTS_FETCHED, commentService.getCommentsForUpdate(dailyUpdateId, pageable))
        );
    }

    @Operation(summary = SwaggerMessages.DELETE_COMMENT_ADMIN, description = SwaggerMessages.DELETE_COMMENT_ADMIN_DESC)
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse> deleteAdminComment(
            @PathVariable UUID commentId) {

        commentService.deleteComment(commentId, true);

        return ResponseEntity.ok(
                new BaseResponse(Messages.COMMENT_DELETED, null)
        );
    }
}