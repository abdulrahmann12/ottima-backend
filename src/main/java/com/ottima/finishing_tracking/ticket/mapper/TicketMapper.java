package com.ottima.finishing_tracking.ticket.mapper;

import com.ottima.finishing_tracking.ticket.dto.request.CreateTicketRequest;
import com.ottima.finishing_tracking.ticket.dto.request.TicketAttachmentRequest;
import com.ottima.finishing_tracking.ticket.dto.request.UpdateTicketRequest;
import com.ottima.finishing_tracking.ticket.dto.response.TicketAttachmentResponse;
import com.ottima.finishing_tracking.ticket.dto.response.TicketResponse;
import com.ottima.finishing_tracking.ticket.entity.InternalTicket;
import com.ottima.finishing_tracking.ticket.entity.TicketAttachment;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    @Mapping(target = "ticketId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "receiver", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    InternalTicket toEntity(CreateTicketRequest request);

    @Mapping(target = "projectId", source = "project.projectId")
    @Mapping(target = "senderId", source = "sender.userId")
    @Mapping(target = "senderNameAr", source = "sender.fullNameAr")
    @Mapping(target = "senderNameEn", source = "sender.fullNameEn")
    @Mapping(target = "senderRole", source = "sender.role.roleName")
    @Mapping(target = "receiverId", source = "receiver.userId")
    @Mapping(target = "receiverNameAr", source = "receiver.fullNameAr")
    @Mapping(target = "receiverNameEn", source = "receiver.fullNameEn")
    @Mapping(target = "receiverRole", source = "receiver.role.roleName")
    TicketResponse toResponse(InternalTicket entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "attachments", ignore = true)
    void updateEntityFromRequest(UpdateTicketRequest request, @MappingTarget InternalTicket entity);

    @Mapping(target = "ticketAttachmentId", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    TicketAttachment toAttachmentEntity(TicketAttachmentRequest request);

    TicketAttachmentResponse toAttachmentResponse(TicketAttachment entity);
}