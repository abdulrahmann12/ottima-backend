package com.ottima.finishing_tracking.role.mapper;

import com.ottima.finishing_tracking.role.dto.CreateRoleRequest;
import com.ottima.finishing_tracking.role.dto.RoleResponse;
import com.ottima.finishing_tracking.role.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Role toEntity(CreateRoleRequest request);

    RoleResponse toResponse(Role role);
}
