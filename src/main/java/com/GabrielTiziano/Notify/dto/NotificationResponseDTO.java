package com.GabrielTiziano.Notify.dto;

import com.GabrielTiziano.Notify.model.enums.NotificationChannel;
import com.GabrielTiziano.Notify.model.enums.NotificationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponseDTO (
        String id,
        String recipient,
        NotificationChannel channel,
        NotificationStatus status,
        LocalDateTime createdAt
){
}
