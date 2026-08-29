package com.ottima.finishing_tracking.comment.mapper;

import com.ottima.finishing_tracking.comment.dto.response.CommentResponse;
import com.ottima.finishing_tracking.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "dailyUpdateId", source = "dailyUpdate.dailyUpdateId")
    @Mapping(target = "clientNameAr", source = "client.fullNameAr")
    @Mapping(target = "clientNameEn", source = "client.fullNameEn")
    @Mapping(target = "repliedByAdminNameAr", source = "repliedByAdmin.fullNameAr")
    @Mapping(target = "repliedByAdminNameEn", source = "repliedByAdmin.fullNameEn")
    CommentResponse toResponse(Comment entity);
}