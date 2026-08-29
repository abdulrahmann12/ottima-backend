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
@Schema(description = "Request body for updating an existing financial record")
public class UpdateFinancialRecordRequest {

    @Schema(description = "ID of the project item (null to unlink from any item)", example = "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d")
    private UUID projectItemId;

    @Schema(description = "Updated type of the financial record: DEPOSIT or EXPENSE", example = "EXPENSE")
    @NotNull(message = ValidationMessages.FINANCIAL_RECORD_TYPE_REQUIRED)
    private RecordType recordType;

    @Schema(description = "Updated monetary amount of the transaction", example = "7500.00")
    @NotNull(message = ValidationMessages.FINANCIAL_AMOUNT_REQUIRED)
    @Positive(message = ValidationMessages.FINANCIAL_AMOUNT_POSITIVE)
    private BigDecimal amount;

    @Schema(description = "Updated payment method for this transaction", example = "CASH")
    @NotNull(message = ValidationMessages.FINANCIAL_PAYMENT_METHOD_REQUIRED)
    private PaymentMethod paymentMethod;

    @Schema(description = "Updated date of the transaction", example = "2026-09-01")
    @NotNull(message = ValidationMessages.FINANCIAL_TRANSACTION_DATE_REQUIRED)
    private LocalDate transactionDate;

    @Schema(description = "Updated URL of the supporting document", example = "https://res.cloudinary.com/.../receipt2.pdf")
    @Size(max = 2048, message = ValidationMessages.FINANCIAL_DOCUMENT_URL_SIZE)
    private String documentUrl;

    @Schema(description = "Updated type of the attached document", example = "RECEIPT")
    private DocumentType documentType;

    @Schema(description = "Updated notes or remarks about this transaction", example = "Revised payment for additional materials.")
    @Size(max = 500, message = ValidationMessages.FINANCIAL_NOTES_SIZE)
    private String notes;
}