package com.ottima.finishing_tracking.standard_item.mapper;

import com.ottima.finishing_tracking.standard_item.dto.request.StandardItemRequest;
import com.ottima.finishing_tracking.standard_item.dto.response.StandardItemResponse;
import com.ottima.finishing_tracking.standard_item.entity.StandardItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StandardItemMapper {

    @Mapping(target = "itemId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletesAt", ignore = true)
    StandardItem toEntity(StandardItemRequest request);

    StandardItemResponse toResponse(StandardItem entity);

    @Mapping(target = "itemId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletesAt", ignore = true)
    void updateEntityFromRequest(StandardItemRequest request, @MappingTarget StandardItem entity);
}