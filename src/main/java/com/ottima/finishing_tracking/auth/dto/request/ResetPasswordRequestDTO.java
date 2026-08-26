package com.ottima.finishing_tracking.auth.dto.request;
import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequestDTO {
    @NotBlank(message = ValidationMessages.USERNAME_OR_EMAIL_OR_PHONE_NUMBER_REQUIRED)
    private String usernameOrEmailOrPhoneNumber;

    @NotBlank(message = ValidationMessages.VERIFICATION_CODE)
    private String code;

    @NotBlank(message = ValidationMessages.NEW_PASSWORD_REQUIRED)
    @Size(min = 8, message = ValidationMessages.NEW_PASSWORD_MIN_SIZE)
    private String newPassword;
}
