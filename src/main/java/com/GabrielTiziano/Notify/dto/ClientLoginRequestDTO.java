package com.GabrielTiziano.Notify.dto;

import jakarta.validation.constraints.NotBlank;

public record ClientLoginRequestDTO(
        @NotBlank
        String email,

        @NotBlank
        String password) {
}
