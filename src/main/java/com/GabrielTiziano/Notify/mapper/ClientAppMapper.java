package com.GabrielTiziano.Notify.mapper;

import com.GabrielTiziano.Notify.dto.ClientRegisterRequestDTO;
import com.GabrielTiziano.Notify.dto.ClientResponseDTO;
import com.GabrielTiziano.Notify.model.ClientAppModel;

public class ClientAppMapper {
        public static ClientAppModel toModel(ClientRegisterRequestDTO clientRegisterRequestDTO){
            return ClientAppModel.builder()
                    .clientName(clientRegisterRequestDTO.clientName())
                    .email(clientRegisterRequestDTO.email())
                    .password(clientRegisterRequestDTO.password())
                    .build();
        }

        public static ClientResponseDTO toClienteResponseDTO(ClientAppModel clientAppModel){
            return ClientResponseDTO.builder()
                    .id(clientAppModel.getId())
                    .clientName(clientAppModel.getClientName())
                    .email(clientAppModel.getEmail())
                    .build();
        }
}
