package com.GabrielTiziano.Notify.mapper;

import com.GabrielTiziano.Notify.dto.NotificationRequestDTO;
import com.GabrielTiziano.Notify.dto.NotificationResponseDTO;
import com.GabrielTiziano.Notify.model.NotificationModel;
import com.GabrielTiziano.Notify.model.enums.NotificationStatus;

import java.time.LocalDateTime;

public class NotificationMapper {
    public static NotificationModel toModel(NotificationRequestDTO notificationRequestDTO){
        return  NotificationModel.builder()
                .recipient(notificationRequestDTO.recipient())
                .subject(notificationRequestDTO.subject())
                .body(notificationRequestDTO.body())
                .channel(notificationRequestDTO.channel())
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static NotificationResponseDTO toNotificationResponseDTO(NotificationModel notificationModel){
        return NotificationResponseDTO.builder()
                .id(notificationModel.getId())
                .recipient(notificationModel.getRecipient())
                .status(notificationModel.getStatus())
                .channel(notificationModel.getChannel())
                .createdAt(notificationModel.getCreatedAt())
                .build();
    }
}
