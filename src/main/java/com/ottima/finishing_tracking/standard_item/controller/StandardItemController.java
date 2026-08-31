package com.ottima.finishing_tracking.standard_item.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
import com.ottima.finishing_tracking.standard_item.dto.request.StandardItemRequest;
import com.ottima.finishing_tracking.standard_item.service.StandardItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/standard-items")
@RequiredArgsConstructor
@Tag(name = SwaggerMessages.TAG_STANDARD_ITEM, description = SwaggerMessages.TAG_STANDARD_ITEM_DESC)
public class StandardItemController {

    private final StandardItemService standardItemService;

    @Operation(summary = SwaggerMessages.CREATE_STANDARD_ITEM)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BaseResponse> create(@Valid @RequestBody StandardItemRequest request) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.STANDARD_ITEM_CREATED, standardItemService.create(request))
        );
    }

    @Operation(summary = SwaggerMessages.UPDATE_STANDARD_ITEM)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{itemId}")
    public ResponseEntity<BaseResponse> update(
            @PathVariable UUID itemId,
            @Valid @RequestBody StandardItemRequest request) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.STANDARD_ITEM_UPDATED, standardItemService.update(itemId, request))
        );
    }

    @Operation(summary = SwaggerMessages.GET_STANDARD_ITEM)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{itemId}")
    public ResponseEntity<BaseResponse> getById(@PathVariable UUID itemId) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.STANDARD_ITEM_FETCHED, standardItemService.getById(itemId))
        );
    }

    @Operation(summary = SwaggerMessages.GET_ALL_STANDARD_ITEMS)
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<BaseResponse> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                new BaseResponse(Messages.STANDARD_ITEMS_FETCHED, standardItemService.getAll(search,page, size))
        );
    }

    @Operation(summary = SwaggerMessages.DELETE_STANDARD_ITEM)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<BaseResponse> delete(@PathVariable UUID itemId) {
        standardItemService.delete(itemId);
        return ResponseEntity.ok(
                new BaseResponse(Messages.STANDARD_ITEM_DELETED)
        );
    }
}