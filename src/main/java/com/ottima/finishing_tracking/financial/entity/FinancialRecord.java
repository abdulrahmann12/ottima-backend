package com.ottima.finishing_tracking.financial.entity;

import com.ottima.finishing_tracking.financial.enums.DocumentType;
import com.ottima.finishing_tracking.financial.enums.PaymentMethod;
import com.ottima.finishing_tracking.financial.enums.RecordType;
import com.ottima.finishing_tracking.project.entity.Project;
import com.ottima.finishing_tracking.project.entity.ProjectItem;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "financial_records", indexes = {
        @Index(name = "idx_fin_proj_type", columnList = "project_id, record_type"),
        @Index(name = "idx_fin_proj_date", columnList = "project_id, transaction_date DESC")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID financialRecordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_item_id")
    private ProjectItem projectItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "record_type", nullable = false)
    private RecordType recordType;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "document_url")
    private String documentUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType documentType;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FinancialRecord)) return false;
        FinancialRecord financialRecord = (FinancialRecord) o;
        return financialRecordId != null && financialRecordId.equals(financialRecord.financialRecordId);
    }
    @Override
    public int hashCode() { return getClass().hashCode(); }
}