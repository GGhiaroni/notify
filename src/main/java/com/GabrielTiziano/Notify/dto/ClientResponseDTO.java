package com.GabrielTiziano.Notify.dto;

import lombok.Builder;

@Builder
public record ClientResponseDTO(
        String id,
        String clientName,
        String email
) {
}
