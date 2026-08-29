package com.ottima.finishing_tracking.logging.mapper;

import com.ottima.finishing_tracking.logging.dto.response.ActivityLogResponse;
import com.ottima.finishing_tracking.logging.entity.UserActivityLog;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityLogMapper {

    ActivityLogResponse toResponse(UserActivityLog entity);
}