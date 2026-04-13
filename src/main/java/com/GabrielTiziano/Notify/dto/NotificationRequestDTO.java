package com.GabrielTiziano.Notify.dto;

import com.GabrielTiziano.Notify.model.enums.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationRequestDTO(
        @NotBlank
        String recipient,

        @NotNull
        NotificationChannel channel,

        String subject,

        @NotBlank
        String body
) {
}
