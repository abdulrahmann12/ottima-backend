package com.ottima.finishing_tracking.engineer.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
import com.ottima.finishing_tracking.engineer.service.EngineerService;
import com.ottima.finishing_tracking.user.dto.request.CreateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/engineers")
@RequiredArgsConstructor
@Tag(name = SwaggerMessages.TAG_ENGINEER, description = SwaggerMessages.TAG_ENGINEER_DESC)
public class EngineerController {

    private final EngineerService engineerService;

    @Operation(summary = SwaggerMessages.CREATE_ENGINEER_USER, description = SwaggerMessages.CREATE_ENGINEER_USER_DESC)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BaseResponse> createEngineer(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.ENGINEER_CREATED, engineerService.createEngineer(request))
        );
    }

    @Operation(summary = SwaggerMessages.GET_ALL_ENGINEERS, description = SwaggerMessages.GET_ALL_ENGINEERS_DESC)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<BaseResponse> getAllEngineers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.ENGINEERS_FETCHED, engineerService.getAllEngineers(page, size))
        );
    }
}