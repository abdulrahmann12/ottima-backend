package com.ottima.finishing_tracking.user.mapper;

import com.ottima.finishing_tracking.user.dto.request.AdminUpdateUserRequest;
import com.ottima.finishing_tracking.user.dto.request.CreateUserRequest;
import com.ottima.finishing_tracking.user.dto.request.UpdateProfileRequest;
import com.ottima.finishing_tracking.user.dto.response.UserResponse;
import com.ottima.finishing_tracking.user.dto.response.UserSummaryResponse;
import com.ottima.finishing_tracking.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletesAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "requestCode", ignore = true)
    @Mapping(target = "requestCodeExpiresAt", ignore = true)
    User toEntity(CreateUserRequest createUserRequest);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletesAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "requestCode", ignore = true)
    @Mapping(target = "requestCodeExpiresAt", ignore = true)
    User toUpdateEntity(AdminUpdateUserRequest updateUserRequest);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletesAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "requestCode", ignore = true)
    @Mapping(target = "requestCodeExpiresAt", ignore = true)
    User toUpdateEntity(UpdateProfileRequest updateUserRequest);

    @Mapping(target = "roleName", source = "role.roleName")
    UserResponse toResponse(User user);

    @Mapping(target = "roleName", source = "role.roleName")
    UserSummaryResponse toSummaryResponse(User user);
}
