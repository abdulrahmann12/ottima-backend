package com.ottima.finishing_tracking.admin.service;

import com.ottima.finishing_tracking.admin.dto.response.DashboardSummaryResponse;
import com.ottima.finishing_tracking.user.dto.request.CreateUserRequest;
import com.ottima.finishing_tracking.user.dto.response.UserResponse;
import com.ottima.finishing_tracking.user.dto.response.UserSummaryResponse;
import com.ottima.finishing_tracking.user.entity.User;
import com.ottima.finishing_tracking.user.mapper.UserMapper;
import com.ottima.finishing_tracking.user.repository.UserRepository;
import com.ottima.finishing_tracking.user.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class AdminService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Transactional
    public UserResponse createAdmin(@Valid CreateUserRequest request) {
        User savedAdmin = userService.createBaseUser(request, "ADMIN");
        return userMapper.toResponse(savedAdmin);
    }

    public Page<UserSummaryResponse> getAllAdmins(int page, int size) {
        return userService.getUsersByRole("ADMIN", page, size);
    }

    public DashboardSummaryResponse getDashboardSummary() {

        long totalActiveUsers = userRepository.countByActiveTrue();
        long totalDeactivatedUsers = userRepository.countByActiveFalse();

        long totalClients = 0;
        long totalEngineers = 0;
        long activeProjects = 0;
        long completedProjects = 0;

        return DashboardSummaryResponse.builder()
                .totalActiveUsers(totalActiveUsers)
                .totalDeactivatedUsers(totalDeactivatedUsers)
                .totalClients(totalClients)
                .totalEngineers(totalEngineers)
                .activeProjects(activeProjects)
                .completedProjects(completedProjects)
                .build();
    }
}