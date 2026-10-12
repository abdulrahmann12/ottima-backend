package com.ottima.finishing_tracking.project_chat.mapper;

import com.ottima.finishing_tracking.project_chat.dto.response.ProjectChatMessageResponse;
import com.ottima.finishing_tracking.project_chat.entity.ProjectChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectChatMapper {

    @Mapping(target = "projectId", source = "project.projectId")
    @Mapping(target = "senderId", source = "sender.userId")
    ProjectChatMessageResponse toResponse(ProjectChatMessage entity);
}
