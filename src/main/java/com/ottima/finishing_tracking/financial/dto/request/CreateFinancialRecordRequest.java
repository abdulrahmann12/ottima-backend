package com.ottima.finishing_tracking.financial.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import com.ottima.finishing_tracking.financial.enums.DocumentType;
import com.ottima.finishing_tracking.financial.enums.PaymentMethod;
import com.ottima.finishing_tracking.financial.enums.RecordType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for creating a new financial record (deposit or expense) for a project")
public class CreateFinancialRecordRequest {

    @Schema(description = "ID of the project item (null for general project-level deposits)", example = "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d")
    private UUID projectItemId;

    @Schema(description = "Type of the financial record: DEPOSIT or EXPENSE", example = "DEPOSIT")
    @NotNull(message = ValidationMessages.FINANCIAL_RECORD_TYPE_REQUIRED)
    private RecordType recordType;

    @Schema(description = "Monetary amount of the transaction", example = "5000.00")
    @NotNull(message = ValidationMessages.FINANCIAL_AMOUNT_REQUIRED)
    @Positive(message = ValidationMessages.FINANCIAL_AMOUNT_POSITIVE)
    private BigDecimal amount;

    @Schema(description = "Payment method used for this transaction", example = "BANK_TRANSFER")
    @NotNull(message = ValidationMessages.FINANCIAL_PAYMENT_METHOD_REQUIRED)
    private PaymentMethod paymentMethod;

    @Schema(description = "Date of the transaction", example = "2026-08-28")
    @NotNull(message = ValidationMessages.FINANCIAL_TRANSACTION_DATE_REQUIRED)
    private LocalDate transactionDate;

    @Schema(description = "URL of the supporting document (invoice, receipt, etc.)", example = "https://res.cloudinary.com/.../invoice1.pdf")
    @Size(max = 2048, message = ValidationMessages.FINANCIAL_DOCUMENT_URL_SIZE)
    private String documentUrl;

    @Schema(description = "Type of the attached document", example = "INVOICE")
    private DocumentType documentType;

    @Schema(description = "Optional notes or remarks about this transaction", example = "First payment installment for tiles.")
    @Size(max = 500, message = ValidationMessages.FINANCIAL_NOTES_SIZE)
    private String notes;
}