package com.ottima.finishing_tracking.financial.service;

import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.common.messages.Constants;
import com.ottima.finishing_tracking.logging.annotation.LogActivity;
import com.ottima.finishing_tracking.logging.enums.ActionType;
import com.ottima.finishing_tracking.exception.*;
import com.ottima.finishing_tracking.financial.dto.request.CreateFinancialRecordRequest;
import com.ottima.finishing_tracking.financial.dto.request.UpdateFinancialRecordRequest;
import com.ottima.finishing_tracking.financial.dto.response.FinancialRecordResponse;
import com.ottima.finishing_tracking.financial.dto.response.FinancialSummaryResponse;
import com.ottima.finishing_tracking.financial.entity.FinancialRecord;
import com.ottima.finishing_tracking.financial.enums.RecordType;
import com.ottima.finishing_tracking.financial.mapper.FinancialRecordMapper;
import com.ottima.finishing_tracking.financial.repository.FinancialRecordRepository;
import com.ottima.finishing_tracking.project.entity.Project;
import com.ottima.finishing_tracking.project.entity.ProjectItem;
import com.ottima.finishing_tracking.project.repository.ProjectItemRepository;
import com.ottima.finishing_tracking.project.repository.ProjectRepository;
import com.ottima.finishing_tracking.security.AuthenticatedUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class FinancialService {

    private final FinancialRecordRepository financialRecordRepository;
    private final ProjectRepository projectRepository;
    private final ProjectItemRepository projectItemRepository;
    private final FinancialRecordMapper financialRecordMapper;
    private final AuthenticatedUserService authenticatedUserService;

    @LogActivity(actionType = ActionType.CREATE, entityName = Constants.FINANCIAL_RECORD_ENTITY, details = Messages.FINANCIAL_RECORD_CREATED_LOG)
    @Transactional
    @CacheEvict(value = {"financialSummary", "projectFinancials"}, allEntries = true)
    public FinancialRecordResponse createFinancialRecord(UUID projectId, @Valid CreateFinancialRecordRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        ProjectItem projectItem = null;
        if (request.getProjectItemId() != null) {
            projectItem = projectItemRepository.findByProjectItemIdAndProject_ProjectId(request.getProjectItemId(), projectId)
                    .orElseThrow(ProjectItemNotFoundException::new);
        }

        FinancialRecord record = financialRecordMapper.toEntity(request);
        record.setProject(project);
        record.setProjectItem(projectItem);

        FinancialRecord savedRecord = financialRecordRepository.save(record);
        return financialRecordMapper.toResponse(savedRecord);
    }

    @LogActivity(actionType = ActionType.UPDATE, entityName = Constants.FINANCIAL_RECORD_ENTITY, details = Messages.FINANCIAL_RECORD_UPDATED_LOG)
    @Transactional
    @CacheEvict(value = {"financialSummary", "projectFinancials"}, allEntries = true)
    public FinancialRecordResponse updateFinancialRecord(UUID financialRecordId, @Valid UpdateFinancialRecordRequest request) {
        FinancialRecord record = financialRecordRepository.findById(financialRecordId)
                .orElseThrow(FinancialRecordNotFoundException::new);

        if (request.getProjectItemId() != null) {
            ProjectItem projectItem = projectItemRepository.findById(request.getProjectItemId())
                    .orElseThrow(ProjectItemNotFoundException::new);

            if (!projectItem.getProject().getProjectId().equals(record.getProject().getProjectId())) {
                throw new ProjectItemMismatchException();
            }
            record.setProjectItem(projectItem);
        } else {
            record.setProjectItem(null);
        }

        record.setRecordType(request.getRecordType());
        record.setAmount(request.getAmount());
        record.setDocumentUrl(request.getDocumentUrl());
        record.setDocumentType(request.getDocumentType());
        record.setNotes(request.getNotes());
        record.setPaymentMethod(request.getPaymentMethod());
        record.setTransactionDate(request.getTransactionDate());

        FinancialRecord updatedRecord = financialRecordRepository.save(record);
        return financialRecordMapper.toResponse(updatedRecord);
    }

    @LogActivity(actionType = ActionType.DELETE, entityName = Constants.FINANCIAL_RECORD_ENTITY, details = Messages.FINANCIAL_RECORD_DELETED_LOG)
    @Transactional
    @CacheEvict(value = {"financialSummary", "projectFinancials"}, allEntries = true)
    public void deleteFinancialRecord(UUID financialRecordId) {
        FinancialRecord record = financialRecordRepository.findById(financialRecordId)
                .orElseThrow(FinancialRecordNotFoundException::new);

        financialRecordRepository.delete(record);
    }

    @Cacheable(value = "financialSummary", key = "#projectId")
    public FinancialSummaryResponse getProjectFinancialSummary(UUID projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException();
        }

        BigDecimal totalPaid = BigDecimal.ZERO;
        BigDecimal totalSpent = BigDecimal.ZERO;
        long totalPaidCount = 0L;
        long totalSpentCount = 0L;

        List<Object[]> summaryResults = financialRecordRepository.getFinancialSummaryByProject(projectId);

        for (Object[] result : summaryResults) {
            RecordType type = (RecordType) result[0];

            long count = result[1] != null ? ((Number) result[1]).longValue() : 0L;
            BigDecimal sum = result[2] != null ? (BigDecimal) result[2] : BigDecimal.ZERO;

            if (type == RecordType.DEPOSIT) {
                totalPaidCount = count;
                totalPaid = sum;
            } else if (type == RecordType.EXPENSE) {
                totalSpentCount = count;
                totalSpent = sum;
            }
        }

        BigDecimal remainingBalance = totalPaid.subtract(totalSpent);

        return FinancialSummaryResponse.builder()
                .totalPaidAmount(totalPaid)
                .totalPaidCount(totalPaidCount)
                .totalSpentAmount(totalSpent)
                .totalSpentCount(totalSpentCount)
                .remainingBalance(remainingBalance)
                .build();
    }

    @Cacheable(value = "projectFinancials", key = "'gallery-' + #projectId + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<FinancialRecordResponse> getProjectInvoicesGallery(UUID projectId, Pageable pageable) {
        return financialRecordRepository.findByProject_ProjectIdAndDocumentUrlIsNotNullOrderByCreatedAtDesc(projectId, pageable)
                .map(financialRecordMapper::toResponse);
    }

    @Cacheable(value = "projectFinancials", key = "'all-' + #projectId + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<FinancialRecordResponse> getAllRecordsByProject(UUID projectId, Pageable pageable) {
        return financialRecordRepository.findByProject_ProjectIdOrderByCreatedAtDesc(projectId, pageable)
                .map(financialRecordMapper::toResponse);
    }

    public FinancialRecordResponse getRecordById(UUID financialRecordId) {
        FinancialRecord record = financialRecordRepository.findById(financialRecordId)
                .orElseThrow(FinancialRecordNotFoundException::new);
        return financialRecordMapper.toResponse(record);
    }

    public Page<FinancialRecordResponse> getRecordsByType(RecordType recordType, Pageable pageable) {
        return financialRecordRepository.findByRecordTypeOrderByCreatedAtDesc(recordType, pageable)
                .map(financialRecordMapper::toResponse);
    }

    @Cacheable(value = "projectFinancials", key = "'type-' + #projectId + '-' + #recordType + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<FinancialRecordResponse> getRecordsByProjectAndType(UUID projectId, RecordType recordType, Pageable pageable) {
        return financialRecordRepository.findByProject_ProjectIdAndRecordTypeOrderByCreatedAtDesc(projectId, recordType, pageable)
                .map(financialRecordMapper::toResponse);
    }

    // ==========================================
    // === Client GET Methods (With Security) ===
    // ==========================================

    private void validateClientProjectOwnership(UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        Long currentUserId = authenticatedUserService.getCurrentUser().getUserId();
        if (!project.getClient().getUserId().equals(currentUserId)) {
            throw new ProjectAccessDeniedException();
        }
    }

    public Page<FinancialRecordResponse> getClientRecordsByProject(UUID projectId, Pageable pageable) {
        validateClientProjectOwnership(projectId);
        return getAllRecordsByProject(projectId, pageable);
    }

    public FinancialRecordResponse getClientRecordById(UUID projectId, UUID financialRecordId) {
        validateClientProjectOwnership(projectId);

        FinancialRecord record = financialRecordRepository.findById(financialRecordId)
                .orElseThrow(FinancialRecordNotFoundException::new);

        if (!record.getProject().getProjectId().equals(projectId)) {
            throw new ProjectAccessDeniedException();
        }

        return financialRecordMapper.toResponse(record);
    }

    public Page<FinancialRecordResponse> getClientRecordsByProjectAndType(UUID projectId, RecordType recordType, Pageable pageable) {
        validateClientProjectOwnership(projectId);
        return getRecordsByProjectAndType(projectId, recordType, pageable);
    }
}