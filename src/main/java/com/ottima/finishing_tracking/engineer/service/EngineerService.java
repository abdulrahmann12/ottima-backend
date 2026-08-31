package com.ottima.finishing_tracking.engineer.service;

import com.ottima.finishing_tracking.user.dto.request.CreateUserRequest;
import com.ottima.finishing_tracking.user.dto.response.UserResponse;
import com.ottima.finishing_tracking.user.dto.response.UserSummaryResponse;
import com.ottima.finishing_tracking.user.entity.User;
import com.ottima.finishing_tracking.user.mapper.UserMapper;
import com.ottima.finishing_tracking.user.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class EngineerService {

    private final UserService userService;
    private final UserMapper userMapper;

    @Transactional
    @CacheEvict(value = "engineersList", allEntries = true)
    public UserResponse createEngineer(@Valid CreateUserRequest request) {
        User savedEngineer = userService.createBaseUser(request, "ENGINEER");
        return userMapper.toResponse(savedEngineer);
    }

    @Cacheable(value = "engineersList", key = "#page + '-' + #size")
    public Page<UserSummaryResponse> getAllEngineers(int page, int size) {
        return userService.getUsersByRole("ENGINEER", page, size);
    }
}