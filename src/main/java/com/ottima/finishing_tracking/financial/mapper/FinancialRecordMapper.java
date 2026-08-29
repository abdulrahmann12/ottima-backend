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
    @Mapping(target = "itemNameAr", source = "projectItem.standardItem.nameAr")
    @Mapping(target = "itemNameEn", source = "projectItem.standardItem.nameEn")
    FinancialRecordResponse toResponse(FinancialRecord entity);
}