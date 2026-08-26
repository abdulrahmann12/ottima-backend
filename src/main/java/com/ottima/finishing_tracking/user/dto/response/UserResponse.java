package com.ottima.finishing_tracking.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long userId;
    private String username;
    private String email;
    private String fullNameAr;
    private String fullNameEn;
    private String phoneNumber;
    private String roleName;
    private String requestCode;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}