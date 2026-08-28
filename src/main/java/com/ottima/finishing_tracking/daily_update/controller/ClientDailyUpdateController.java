package com.ottima.finishing_tracking.daily_update.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
import com.ottima.finishing_tracking.daily_update.service.DailyUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/client/items/{projectItemId}/daily-updates")
@RequiredArgsConstructor
@Tag(name = SwaggerMessages.TAG_DAILY_UPDATE_CLIENT, description = SwaggerMessages.TAG_DAILY_UPDATE_CLIENT_DESC)
@PreAuthorize("hasRole('CLIENT')")
public class ClientDailyUpdateController {

    private final DailyUpdateService dailyUpdateService;

    @Operation(summary = SwaggerMessages.GET_APPROVED_ITEM_UPDATES_CLIENT, description = SwaggerMessages.GET_APPROVED_ITEM_UPDATES_CLIENT_DESC)
    @GetMapping
    public ResponseEntity<BaseResponse> getClientUpdates(
            @PathVariable UUID projectItemId,
            Pageable pageable) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.DAILY_UPDATES_FETCHED,
                        dailyUpdateService.getUpdatesForClient(projectItemId, pageable))
        );
    }
}