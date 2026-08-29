package com.ottima.finishing_tracking.financial.repository;

import com.ottima.finishing_tracking.financial.entity.FinancialRecord;
import com.ottima.finishing_tracking.financial.enums.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, UUID> {

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinancialRecord f WHERE f.project.projectId = :projectId AND f.recordType = :recordType")
    BigDecimal sumAmountByProjectIdAndRecordType(@Param("projectId") UUID projectId, @Param("recordType") RecordType recordType);

    long countByProject_ProjectIdAndRecordType(UUID projectId, RecordType recordType);

    @EntityGraph(attributePaths = {"projectItem", "projectItem.standardItem"})
    Page<FinancialRecord> findByProject_ProjectIdOrderByCreatedAtDesc(UUID projectId, Pageable pageable);

    @EntityGraph(attributePaths = {"projectItem", "projectItem.standardItem"})
    Page<FinancialRecord> findByProject_ProjectIdAndDocumentUrlIsNotNullOrderByCreatedAtDesc(UUID projectId, Pageable pageable);


    @EntityGraph(attributePaths = {"project", "projectItem", "projectItem.standardItem"})
    Optional<FinancialRecord> findById(UUID id);

    @EntityGraph(attributePaths = {"project", "projectItem", "projectItem.standardItem"})
    Page<FinancialRecord> findByRecordTypeOrderByCreatedAtDesc(RecordType recordType, Pageable pageable);

    @EntityGraph(attributePaths = {"project", "projectItem", "projectItem.standardItem"})
    Page<FinancialRecord> findByProject_ProjectIdAndRecordTypeOrderByCreatedAtDesc(UUID projectId, RecordType recordType, Pageable pageable);
}