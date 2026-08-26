package com.ottima.finishing_tracking.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryResponse {

    private Long userId;
    private String username;
    private String email;
    private String roleName;
    private String fullNameAr;
    private String fullNameEn;
    private String phoneNumber;
}