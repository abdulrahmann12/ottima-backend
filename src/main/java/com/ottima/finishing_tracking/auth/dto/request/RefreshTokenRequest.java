package com.ottima.finishing_tracking.auth.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {

    @NotBlank(message = ValidationMessages.REFRESH_TOKEN_REQUIRED)
    private String refreshToken;
}