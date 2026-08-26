package com.ottima.finishing_tracking.auth.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = ValidationMessages.USERNAME_OR_EMAIL_OR_PHONE_NUMBER_REQUIRED)
    private String usernameOrEmailOrNumber;

    @NotBlank(message = ValidationMessages.PASSWORD_REQUIRED)
    private String password;
}