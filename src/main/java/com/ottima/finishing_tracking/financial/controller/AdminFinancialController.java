package com.ottima.finishing_tracking.financial.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
import com.ottima.finishing_tracking.financial.dto.request.CreateFinancialRecordRequest;
import com.ottima.finishing_tracking.financial.dto.request.UpdateFinancialRecordRequest;
import com.ottima.finishing_tracking.financial.enums.RecordType;
import com.ottima.finishing_tracking.financial.service.FinancialService;
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
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = SwaggerMessages.TAG_FINANCIAL_ADMIN, description = SwaggerMessages.TAG_FINANCIAL_ADMIN_DESC)
@PreAuthorize("hasRole('ADMIN')")
public class AdminFinancialController {

    private final FinancialService financialService;

    @Operation(summary = SwaggerMessages.CREATE_FINANCIAL_RECORD, description = SwaggerMessages.CREATE_FINANCIAL_RECORD_DESC)
    @PostMapping("/projects/{projectId}/financial-records")
    public ResponseEntity<BaseResponse> createFinancialRecord(
            @PathVariable UUID projectId,
            @Valid @RequestBody CreateFinancialRecordRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new BaseResponse(Messages.FINANCIAL_RECORD_CREATED, financialService.createFinancialRecord(projectId, request))
        );
    }

    @Operation(summary = SwaggerMessages.UPDATE_FINANCIAL_RECORD, description = SwaggerMessages.UPDATE_FINANCIAL_RECORD_DESC)
    @PutMapping("/financial-records/{financialRecordId}")
    public ResponseEntity<BaseResponse> updateFinancialRecord(
            @PathVariable UUID financialRecordId,
            @Valid @RequestBody UpdateFinancialRecordRequest request) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.FINANCIAL_RECORD_UPDATED, financialService.updateFinancialRecord(financialRecordId, request))
        );
    }

    @Operation(summary = SwaggerMessages.DELETE_FINANCIAL_RECORD, description = SwaggerMessages.DELETE_FINANCIAL_RECORD_DESC)
    @DeleteMapping("/financial-records/{financialRecordId}")
    public ResponseEntity<BaseResponse> deleteFinancialRecord(
            @PathVariable UUID financialRecordId) {

        financialService.deleteFinancialRecord(financialRecordId);
        return ResponseEntity.ok(
                new BaseResponse(Messages.FINANCIAL_RECORD_DELETED, null)
        );
    }

    @Operation(summary = SwaggerMessages.GET_ALL_FINANCIAL_RECORDS_ADMIN, description = SwaggerMessages.GET_ALL_FINANCIAL_RECORDS_ADMIN_DESC)
    @GetMapping("/projects/{projectId}/financial-records")
    public ResponseEntity<BaseResponse> getAllByProject(
            @PathVariable UUID projectId, Pageable pageable) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.FINANCIAL_RECORDS_FETCHED, financialService.getAllRecordsByProject(projectId, pageable))
        );
    }

    @Operation(summary = SwaggerMessages.GET_FINANCIAL_RECORD_BY_ID_ADMIN, description = SwaggerMessages.GET_FINANCIAL_RECORD_BY_ID_ADMIN_DESC)
    @GetMapping("/financial-records/{financialRecordId}")
    public ResponseEntity<BaseResponse> getRecordById(
            @PathVariable UUID financialRecordId) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.FINANCIAL_RECORD_FETCHED, financialService.getRecordById(financialRecordId))
        );
    }

    @Operation(summary = SwaggerMessages.GET_FINANCIAL_RECORDS_BY_TYPE_ALL, description = SwaggerMessages.GET_FINANCIAL_RECORDS_BY_TYPE_ALL_DESC)
    @GetMapping("/financial-records/type/{recordType}")
    public ResponseEntity<BaseResponse> getByTypeAllProjects(
            @PathVariable RecordType recordType, Pageable pageable) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.FINANCIAL_RECORDS_FETCHED, financialService.getRecordsByType(recordType, pageable))
        );
    }

    @Operation(summary = SwaggerMessages.GET_FINANCIAL_RECORDS_BY_TYPE_ONE, description = SwaggerMessages.GET_FINANCIAL_RECORDS_BY_TYPE_ONE_DESC)
    @GetMapping("/projects/{projectId}/financial-records/type/{recordType}")
    public ResponseEntity<BaseResponse> getByTypeOneProject(
            @PathVariable UUID projectId, @PathVariable RecordType recordType, Pageable pageable) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.FINANCIAL_RECORDS_FETCHED, financialService.getRecordsByProjectAndType(projectId, recordType, pageable))
        );
    }
}