package com.ottima.finishing_tracking.standard_item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardItemResponse {
    private UUID itemId;
    private String nameAr;
    private String nameEn;
    private String description;
    private Integer defaultSequence;
    private Instant createdAt;
    private Instant updatedAt;
}