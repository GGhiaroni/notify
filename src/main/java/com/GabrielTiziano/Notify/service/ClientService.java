package com.GabrielTiziano.Notify.service;

import com.GabrielTiziano.Notify.dto.ClientLoginRequestDTO;
import com.GabrielTiziano.Notify.dto.ClientRegisterRequestDTO;
import com.GabrielTiziano.Notify.dto.ClientResponseDTO;
import com.GabrielTiziano.Notify.exception.BusinessRuleException;
import com.GabrielTiziano.Notify.mapper.ClientAppMapper;
import com.GabrielTiziano.Notify.model.ClientAppModel;
import com.GabrielTiziano.Notify.repository.ClientAppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientAppRepository clientAppRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientResponseDTO register(ClientRegisterRequestDTO clientRegisterRequestDTO) {
        if (clientAppRepository.findUserByEmail(clientRegisterRequestDTO.email()).isPresent()) {
            throw new BusinessRuleException("Este e-mail já está cadastrado no sistema.");
        }

        ClientAppModel model = ClientAppMapper.toModel(clientRegisterRequestDTO);

        model.setPassword(passwordEncoder.encode(clientRegisterRequestDTO.password()));

        return ClientAppMapper.toClienteResponseDTO(clientAppRepository.save(model));
    }
}
