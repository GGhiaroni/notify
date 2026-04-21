package com.GabrielTiziano.Notify.controller;

import com.GabrielTiziano.Notify.dto.ClientLoginRequestDTO;
import com.GabrielTiziano.Notify.dto.ClientLoginResponseDTO;
import com.GabrielTiziano.Notify.dto.ClientRegisterRequestDTO;
import com.GabrielTiziano.Notify.dto.ClientResponseDTO;
import com.GabrielTiziano.Notify.model.ClientAppModel;
import com.GabrielTiziano.Notify.security.TokenService;
import com.GabrielTiziano.Notify.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticação", description = "Endpoint para registro de novos clientes e geração de tokens JWT no Login.")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final ClientService clientService;
    private final TokenService tokenService;

    @Operation(summary = "Registra um novo cliente", description = "Cria uma nova conta de cliente na plataforma. O e-mail deve ser único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos no corpo da requisição"),
            @ApiResponse(responseCode = "409", description = "Conflito: O e-mail informado já está cadastrado (Business Rule Violation)")
    })
    @PostMapping("/register")
    public ResponseEntity<ClientResponseDTO> register(@RequestBody ClientRegisterRequestDTO clientRegisterRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.register(clientRegisterRequestDTO));
    }

    @Operation(summary = "Realiza o login de um cliente, autenticado-o ", description = "Valida as credenciais do cliente e retorna um token JWT Bearer com validade de 2 horas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso. Retorna o Token JWT"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas (E-mail ou senha incorretos)"),
            @ApiResponse(responseCode = "400", description = "Formato de requisição incorreto")
    })
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

