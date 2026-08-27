package com.ottima.finishing_tracking.standard_item.dto.request;

import com.ottima.finishing_tracking.common.messages.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardItemRequest {

    @NotBlank(message = ValidationMessages.ITEM_NAME_AR_REQUIRED)
    private String nameAr;

    @NotBlank(message = ValidationMessages.ITEM_NAME_EN_REQUIRED)
    private String nameEn;

    private String description;
}