package com.GabrielTiziano.Notify.controller;

import com.GabrielTiziano.Notify.dto.ClientLoginRequestDTO;
import com.GabrielTiziano.Notify.dto.ClientLoginResponseDTO;
import com.GabrielTiziano.Notify.dto.ClientRegisterRequestDTO;
import com.GabrielTiziano.Notify.dto.ClientResponseDTO;
import com.GabrielTiziano.Notify.model.ClientAppModel;
import com.GabrielTiziano.Notify.security.TokenService;
import com.GabrielTiziano.Notify.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notify/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final ClientService clientService;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<ClientResponseDTO> register(@RequestBody ClientRegisterRequestDTO clientRegisterRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.register(clientRegisterRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<ClientLoginResponseDTO> login(@RequestBody ClientLoginRequestDTO clientLoginRequestDTO){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                clientLoginRequestDTO.email(),
                clientLoginRequestDTO.password()
        );

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        ClientAppModel user = (ClientAppModel) authentication.getPrincipal();

        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(new ClientLoginResponseDTO(token));
    }

}

