package com.ottima.finishing_tracking.user.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    @NotBlank(message = ValidationMessages.USERNAME_NOT_BLANK)
    @Size(min = 6, max = 50, message = ValidationMessages.USERNAME_SIZE)
    private String username;

    @NotBlank(message = ValidationMessages.EMAIL_NOT_BLANK)
    @Email(message = ValidationMessages.EMAIL_INVALID)
    @Size(max = 100, message = ValidationMessages.EMAIL_TOO_LONG)
    private String email;

    @NotBlank(message = ValidationMessages.FULL_NAME_NOT_BLANK_AR)
    private String fullNameAr;

    @NotBlank(message = ValidationMessages.FULL_NAME_NOT_BLANK_EN)
    private String fullNameEn;

    @NotBlank(message = ValidationMessages.PHONE_NUMBER_REQUIRED)
    @Size(min = 11, max = 11, message = ValidationMessages.PHONE_NUMBER_SIZE)
    private String phoneNumber;
}