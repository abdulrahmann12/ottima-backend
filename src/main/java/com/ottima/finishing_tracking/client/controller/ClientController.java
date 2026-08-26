package com.ottima.finishing_tracking.client.controller;

import com.ottima.finishing_tracking.client.service.ClientService;
import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
import com.ottima.finishing_tracking.user.dto.request.CreateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Tag(name = SwaggerMessages.TAG_CLIENT, description = SwaggerMessages.TAG_CLIENT_DESC)
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = SwaggerMessages.CREATE_CLIENT_USER, description = SwaggerMessages.CREATE_CLIENT_USER_DESC)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BaseResponse> createClient(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.CLIENT_CREATED, clientService.createClient(request))
        );
    }

    @Operation(summary = SwaggerMessages.GET_ALL_CLIENTS, description = SwaggerMessages.GET_ALL_CLIENTS_DESC)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<BaseResponse> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.CLIENTS_FETCHED, clientService.getAllClients(page, size))
        );
    }
}