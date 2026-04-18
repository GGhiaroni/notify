package com.GabrielTiziano.Notify.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MailtrapAddressDTO(
        String email,
        String name
) {
}
