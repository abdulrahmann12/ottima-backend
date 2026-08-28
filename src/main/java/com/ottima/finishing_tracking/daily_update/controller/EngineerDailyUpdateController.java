package com.ottima.finishing_tracking.daily_update.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
import com.ottima.finishing_tracking.daily_update.dto.request.CreateDailyUpdateRequest;
import com.ottima.finishing_tracking.daily_update.enums.UpdateStatus;
import com.ottima.finishing_tracking.daily_update.service.DailyUpdateService;
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
@RequestMapping("/api/v1/engineer/projects/{projectId}/daily-updates")
@RequiredArgsConstructor
@Tag(name = SwaggerMessages.TAG_DAILY_UPDATE_ENGINEER, description = SwaggerMessages.TAG_DAILY_UPDATE_ENGINEER_DESC)
@PreAuthorize("hasRole('ENGINEER')")
public class EngineerDailyUpdateController {

    private final DailyUpdateService dailyUpdateService;

    @Operation(summary = SwaggerMessages.CREATE_DAILY_UPDATE, description = SwaggerMessages.CREATE_DAILY_UPDATE_DESC)
    @PostMapping
    public ResponseEntity<BaseResponse> createDailyUpdate(
            @PathVariable UUID projectId,
            @Valid @RequestBody CreateDailyUpdateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new BaseResponse(Messages.DAILY_UPDATE_CREATED, dailyUpdateService.createDailyUpdate(projectId, request))
        );
    }

    @Operation(summary = SwaggerMessages.GET_MY_DAILY_UPDATES, description = SwaggerMessages.GET_MY_DAILY_UPDATES_DESC)
    @GetMapping
    public ResponseEntity<BaseResponse> getMyDailyUpdates(
            @PathVariable UUID projectId,
            @RequestParam(required = false) UUID projectItemId,
            @RequestParam(required = false) UpdateStatus status,
            Pageable pageable) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.DAILY_UPDATES_FETCHED,
                        dailyUpdateService.getMyUpdatesAsEngineer(projectId, projectItemId, status, pageable))
        );
    }
}