package com.ottima.finishing_tracking.notification.mapper;

import com.ottima.finishing_tracking.notification.dto.response.NotificationResponse;
import com.ottima.finishing_tracking.notification.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    @Mapping(target = "notificationId", source = "notificationId")
    NotificationResponse toResponse(Notification entity);
}