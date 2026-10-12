package com.ottima.finishing_tracking.financial.mapper;

import com.ottima.finishing_tracking.financial.dto.request.CreateFinancialRecordRequest;
import com.ottima.finishing_tracking.financial.dto.response.FinancialRecordResponse;
import com.ottima.finishing_tracking.financial.entity.FinancialRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FinancialRecordMapper {

    @Mapping(target = "financialRecordId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "projectItem", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    FinancialRecord toEntity(CreateFinancialRecordRequest request);

    @Mapping(target = "projectItemId", source = "projectItem.projectItemId")
    @Mapping(target = "projectId", source = "project.projectId")
    @Mapping(target = "itemNameAr", expression = "java(resolveItemNameAr(entity))")
    @Mapping(target = "itemNameEn", expression = "java(resolveItemNameEn(entity))")
    FinancialRecordResponse toResponse(FinancialRecord entity);

    default String resolveItemNameAr(FinancialRecord entity) {
        if (entity.getProjectItem() != null && entity.getProjectItem().getStandardItem() != null) {
            return entity.getProjectItem().getStandardItem().getNameAr();
        }
        if (entity.getExpenseCategory() != null && !entity.getExpenseCategory().isBlank()) {
            if ("other".equalsIgnoreCase(entity.getExpenseCategory().trim())) {
                return "أخرى";
            }
            return entity.getExpenseCategory();
        }
        return entity.getRecordType() == com.ottima.finishing_tracking.financial.enums.RecordType.EXPENSE ? "مصروفات عامة أخرى" : "دفعة مشروع عامة";
    }

    default String resolveItemNameEn(FinancialRecord entity) {
        if (entity.getProjectItem() != null && entity.getProjectItem().getStandardItem() != null) {
            return entity.getProjectItem().getStandardItem().getNameEn();
        }
        if (entity.getExpenseCategory() != null && !entity.getExpenseCategory().isBlank()) {
            if ("أخرى".equals(entity.getExpenseCategory().trim()) || "other".equalsIgnoreCase(entity.getExpenseCategory().trim())) {
                return "Other";
            }
            return entity.getExpenseCategory();
        }
        return entity.getRecordType() == com.ottima.finishing_tracking.financial.enums.RecordType.EXPENSE ? "Other General Expense" : "General Project Deposit";
    }
}