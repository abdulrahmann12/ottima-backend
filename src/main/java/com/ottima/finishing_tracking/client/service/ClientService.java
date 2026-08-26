package com.ottima.finishing_tracking.client.service;

import com.ottima.finishing_tracking.user.dto.request.CreateUserRequest;
import com.ottima.finishing_tracking.user.dto.response.UserResponse;
import com.ottima.finishing_tracking.user.dto.response.UserSummaryResponse;
import com.ottima.finishing_tracking.user.entity.User;
import com.ottima.finishing_tracking.user.mapper.UserMapper;
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
public class ClientService {

    private final UserService userService;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse createClient(@Valid CreateUserRequest request) {
        User savedClient = userService.createBaseUser(request, "CLIENT");

        // emailService.sendWelcomeEmail(savedClient.getEmail());

        return userMapper.toResponse(savedClient);
    }

    public Page<UserSummaryResponse> getAllClients(int page, int size) {
        return userService.getUsersByRole("CLIENT", page, size);
    }
}