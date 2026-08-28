package com.ottima.finishing_tracking.daily_update.mapper;

import com.ottima.finishing_tracking.daily_update.dto.request.CreateDailyUpdateRequest;
import com.ottima.finishing_tracking.daily_update.dto.response.DailyUpdateResponse;
import com.ottima.finishing_tracking.daily_update.entity.DailyUpdate;
import com.ottima.finishing_tracking.daily_update.entity.UpdateImage;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface DailyUpdateMapper {

    @Mapping(target = "dailyUpdateId", ignore = true)
    @Mapping(target = "projectItem", ignore = true)
    @Mapping(target = "engineer", ignore = true)
    @Mapping(target = "approvedByAdmin", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    DailyUpdate toEntity(CreateDailyUpdateRequest request);

    @AfterMapping
    default void mapImagesToEntity(CreateDailyUpdateRequest request, @MappingTarget DailyUpdate entity) {
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            if (entity.getImages() == null) {
                entity.setImages(new ArrayList<>());
            }
            for (String url : request.getImageUrls()) {
                UpdateImage image = new UpdateImage();
                image.setImageUrl(url);
                image.setDailyUpdate(entity);
                image.setApproved(null);
                entity.getImages().add(image);
            }
        }
    }

    @AfterMapping
    default void filterImagesForClient(DailyUpdate entity, @MappingTarget DailyUpdateResponse response, @Context boolean isClient) {
        if (isClient && response.getImages() != null) {
            response.setImages(
                    response.getImages().stream()
                            .filter(img -> img.getApproved() == null || img.getApproved())
                            .collect(Collectors.toList())
            );
        }
    }

    @Mapping(target = "dailyUpdateId", source = "dailyUpdateId")
    @Mapping(target = "projectItemId", source = "projectItem.projectItemId")
    @Mapping(target = "itemNameAr", source = "projectItem.standardItem.nameAr")
    @Mapping(target = "itemNameEn", source = "projectItem.standardItem.nameEn")
    @Mapping(target = "engineerUsername", source = "engineer.username")
    @Mapping(target = "engineerNameAr", source = "engineer.fullNameAr")
    @Mapping(target = "engineerNameEn", source = "engineer.fullNameEn")
    @Mapping(target = "approvedByAdminUsername", source = "approvedByAdmin.username")
    @Mapping(target = "approvedByAdminNameAr", source = "approvedByAdmin.fullNameAr")
    @Mapping(target = "approvedByAdminNameEn", source = "approvedByAdmin.fullNameEn")
    @Mapping(target = "images", source = "images")
    DailyUpdateResponse toResponse(DailyUpdate entity, @Context boolean isClient);
}