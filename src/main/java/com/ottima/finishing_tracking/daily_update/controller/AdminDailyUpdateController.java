package com.ottima.finishing_tracking.daily_update.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
import com.ottima.finishing_tracking.daily_update.dto.request.EvaluateDailyUpdateRequest;
import com.ottima.finishing_tracking.daily_update.enums.UpdateStatus;
import com.ottima.finishing_tracking.daily_update.service.DailyUpdateService;
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
@Tag(name = SwaggerMessages.TAG_DAILY_UPDATE_ADMIN, description = SwaggerMessages.TAG_DAILY_UPDATE_ADMIN_DESC)
@PreAuthorize("hasRole('ADMIN')")
public class AdminDailyUpdateController {

    private final DailyUpdateService dailyUpdateService;

    @Operation(summary = SwaggerMessages.GET_ALL_DAILY_UPDATES_ADMIN, description = SwaggerMessages.GET_ALL_DAILY_UPDATES_ADMIN_DESC)
    @GetMapping("/projects/{projectId}/daily-updates")
    public ResponseEntity<BaseResponse> getUpdatesForAdmin(
            @PathVariable UUID projectId,
            @RequestParam(required = false) UUID projectItemId,
            @RequestParam(required = false) Long engineerId,
            @RequestParam(required = false) UpdateStatus status,
            Pageable pageable) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.DAILY_UPDATES_FETCHED,
                        dailyUpdateService.getUpdatesForAdmin(projectId, projectItemId, engineerId, status, pageable))
        );
    }

    @Operation(summary = SwaggerMessages.EVALUATE_DAILY_UPDATE, description = SwaggerMessages.EVALUATE_DAILY_UPDATE_DESC)
    @PutMapping("/daily-updates/{dailyUpdateId}/evaluate")
    public ResponseEntity<BaseResponse> evaluateDailyUpdate(
            @PathVariable UUID dailyUpdateId,
            @Valid @RequestBody EvaluateDailyUpdateRequest request) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.DAILY_UPDATE_EVALUATED,
                        dailyUpdateService.evaluateDailyUpdate(dailyUpdateId, request))
        );
    }
}