package com.ottima.finishing_tracking.financial.dto.response;

import com.ottima.finishing_tracking.financial.enums.DocumentType;
import com.ottima.finishing_tracking.financial.enums.PaymentMethod;
import com.ottima.finishing_tracking.financial.enums.RecordType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Schema(description = "Response object representing a single financial record")
public class FinancialRecordResponse {

    @Schema(description = "Unique ID of the financial record", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID financialRecordId;

    @Schema(description = "ID of the linked project item (null if project-level)", example = "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d")
    private UUID projectItemId;

    @Schema(description = "ID of the linked project (null if project-level)", example = "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d")
    private UUID projectId;

    @Schema(description = "Arabic name of the linked project item", example = "دهانات الحوائط")
    private String itemNameAr;

    @Schema(description = "English name of the linked project item", example = "Wall Painting")
    private String itemNameEn;

    @Schema(description = "Type of the record: DEPOSIT or EXPENSE", example = "DEPOSIT")
    private RecordType recordType;

    @Schema(description = "Monetary amount of the transaction", example = "5000.00")
    private BigDecimal amount;

    @Schema(description = "URL of the supporting document", example = "https://res.cloudinary.com/.../invoice1.pdf")
    private String documentUrl;

    @Schema(description = "Type of the attached document", example = "INVOICE")
    private DocumentType documentType;

    @Schema(description = "Notes or remarks about this transaction", example = "First payment installment for tiles.")
    private String notes;

    @Schema(description = "Payment method used for this transaction", example = "BANK_TRANSFER")
    private PaymentMethod paymentMethod;

    @Schema(description = "Date of the transaction", example = "2026-08-28")
    private LocalDate transactionDate;

    @Schema(description = "Timestamp when the record was created", example = "2026-08-28T18:00:00Z")
    private Instant createdAt;
}