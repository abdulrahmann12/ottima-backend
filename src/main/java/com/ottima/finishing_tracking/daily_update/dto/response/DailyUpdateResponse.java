package com.ottima.finishing_tracking.daily_update.dto.response;

import com.ottima.finishing_tracking.daily_update.enums.UpdateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyUpdateResponse {

    private UUID dailyUpdateId;
    private UUID projectItemId;
    private String itemNameAr;
    private String itemNameEn;
    private String engineerUsername;
    private String engineerNameAr;
    private String engineerNameEn;
    private String approvedByAdminUsername;
    private String approvedByAdminNameAr;
    private String approvedByAdminNameEn;
    private String title;
    private String notes;
    private UpdateStatus status;
    private Instant createdAt;

    private List<UpdateImageResponse> images;

    @Data
    public static class UpdateImageResponse {
        private UUID updateImageId;
        private String imageUrl;
        private Boolean approved;
    }
}