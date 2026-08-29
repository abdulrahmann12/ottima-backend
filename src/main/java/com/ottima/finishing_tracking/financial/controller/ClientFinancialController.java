package com.ottima.finishing_tracking.financial.controller;

import com.ottima.finishing_tracking.common.dto.BaseResponse;
import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.SwaggerMessages;
import com.ottima.finishing_tracking.financial.enums.RecordType;
import com.ottima.finishing_tracking.financial.service.FinancialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/client/projects/{projectId}")
@RequiredArgsConstructor
@Tag(name = SwaggerMessages.TAG_FINANCIAL_CLIENT, description = SwaggerMessages.TAG_FINANCIAL_CLIENT_DESC)
@PreAuthorize("hasRole('CLIENT')")
public class ClientFinancialController {

    private final FinancialService financialService;

    @Operation(summary = SwaggerMessages.GET_FINANCIAL_SUMMARY_CLIENT, description = SwaggerMessages.GET_FINANCIAL_SUMMARY_CLIENT_DESC)
    @GetMapping("/financial-summary")
    public ResponseEntity<BaseResponse> getFinancialSummary(@PathVariable UUID projectId) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.FINANCIAL_SUMMARY_FETCHED, financialService.getProjectFinancialSummary(projectId))
        );
    }

    @Operation(summary = SwaggerMessages.GET_INVOICES_GALLERY_CLIENT, description = SwaggerMessages.GET_INVOICES_GALLERY_CLIENT_DESC)
    @GetMapping("/invoices")
    public ResponseEntity<BaseResponse> getInvoicesGallery(
            @PathVariable UUID projectId,
            Pageable pageable) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.INVOICES_GALLERY_FETCHED, financialService.getProjectInvoicesGallery(projectId, pageable))
        );
    }

    @Operation(summary = SwaggerMessages.GET_ALL_FINANCIAL_RECORDS_CLIENT, description = SwaggerMessages.GET_ALL_FINANCIAL_RECORDS_CLIENT_DESC)
    @GetMapping("/financial-records")
    public ResponseEntity<BaseResponse> getMyProjectRecords(
            @PathVariable UUID projectId, Pageable pageable) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.FINANCIAL_RECORDS_FETCHED, financialService.getClientRecordsByProject(projectId, pageable))
        );
    }

    @Operation(summary = SwaggerMessages.GET_FINANCIAL_RECORD_BY_ID_CLIENT, description = SwaggerMessages.GET_FINANCIAL_RECORD_BY_ID_CLIENT_DESC)
    @GetMapping("/financial-records/{financialRecordId}")
    public ResponseEntity<BaseResponse> getMyProjectRecordById(
            @PathVariable UUID projectId, @PathVariable UUID financialRecordId) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.FINANCIAL_RECORD_FETCHED, financialService.getClientRecordById(projectId, financialRecordId))
        );
    }

    @Operation(summary = SwaggerMessages.GET_FINANCIAL_RECORDS_BY_TYPE_CLIENT, description = SwaggerMessages.GET_FINANCIAL_RECORDS_BY_TYPE_CLIENT_DESC)
    @GetMapping("/financial-records/type/{recordType}")
    public ResponseEntity<BaseResponse> getMyProjectRecordsByType(
            @PathVariable UUID projectId, @PathVariable RecordType recordType, Pageable pageable) {

        return ResponseEntity.ok(
                new BaseResponse(Messages.FINANCIAL_RECORDS_FETCHED, financialService.getClientRecordsByProjectAndType(projectId, recordType, pageable))
        );
    }
}