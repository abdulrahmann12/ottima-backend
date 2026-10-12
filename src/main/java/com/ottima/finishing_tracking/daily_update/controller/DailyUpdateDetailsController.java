package com.ottima.finishing_tracking.daily_update.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.daily_update.service.DailyUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/daily-updates")
@RequiredArgsConstructor
@Tag(name = "Daily Updates Details", description = "Endpoints to fetch daily update details")
public class DailyUpdateDetailsController {

    private final DailyUpdateService dailyUpdateService;

    @Operation(summary = "Get daily update by ID", description = "Fetch daily update full details by its ID")
    @GetMapping("/{dailyUpdateId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BaseResponse> getDailyUpdateById(@PathVariable UUID dailyUpdateId) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.DAILY_UPDATES_FETCHED, dailyUpdateService.getDailyUpdateById(dailyUpdateId))
        );
    }
}
