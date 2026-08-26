package com.ottima.finishing_tracking.auth.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDTO {
    @NotBlank(message = ValidationMessages.USERNAME_OR_EMAIL_OR_PHONE_NUMBER_REQUIRED)
    private String identifier;
}