package com.GabrielTiziano.Notify.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record MailtrapRequestDTO(
        MailtrapAddressDTO from,
        List<MailtrapAddressDTO> to,
        String subject,
        String text
) {
}
