package com.ottima.finishing_tracking.comment.controller;

import com.ottima.finishing_tracking.comment.service.CommentService;
import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.daily_update.service.DailyUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Context Resolution", description = "Endpoints to resolve project and navigation context from entity IDs")
public class ContextResolutionController {

    private final CommentService commentService;
    private final DailyUpdateService dailyUpdateService;

    @Operation(summary = "Resolve comment context", description = "Get project and daily update context for a comment")
    @GetMapping({"/api/v1/comments/{commentId}/context", "/api/v1/client/comments/{commentId}/context"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BaseResponse> getCommentContext(@PathVariable UUID commentId) {
        return ResponseEntity.ok(
                new BaseResponse("Comment context resolved successfully", commentService.resolveCommentContext(commentId))
        );
    }

    @Operation(summary = "Resolve daily update context", description = "Get project and item context for a daily update")
    @GetMapping({"/api/v1/daily-updates/{dailyUpdateId}/context", "/api/v1/client/daily-updates/{dailyUpdateId}/context"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BaseResponse> getDailyUpdateContext(@PathVariable UUID dailyUpdateId) {
        return ResponseEntity.ok(
                new BaseResponse("Daily update context resolved successfully", dailyUpdateService.resolveDailyUpdateContext(dailyUpdateId))
        );
    }
}
