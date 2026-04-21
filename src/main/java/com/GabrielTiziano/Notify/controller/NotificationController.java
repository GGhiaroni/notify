package com.GabrielTiziano.Notify.controller;

import com.GabrielTiziano.Notify.dto.NotificationRequestDTO;
import com.GabrielTiziano.Notify.dto.NotificationResponseDTO;
import com.GabrielTiziano.Notify.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notify/notifications")
@RequiredArgsConstructor
@Tag(name = "Notificações", description = "Endpoints para envio, listagem e consulta de notificações via Mailtrap. Requer Token JWT.")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send")
    @Operation(summary = "Envia uma nova notificação", description = "Dispara um e-mail através do provedor Mailtrap e salva o registro no banco de dados MongoDB vinculado ao cliente autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notificação processada e enviada com sucesso (ou marcada como FAILED caso o provedor falhe)"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos no payload da requisição"),
            @ApiResponse(responseCode = "401", description = "Não autorizado: Token JWT ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "429", description = "Limite de requisições excedido: Máximo de 5 envios por minuto (Redis Rate Limit)")
    })
    public ResponseEntity<NotificationResponseDTO> sendNotification(@RequestBody NotificationRequestDTO notificationRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.sendNotification(notificationRequestDTO));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Busca uma notificação por ID", description = "Retorna os detalhes de uma notificação específica através do seu identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificação encontrada e retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado: Token JWT ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "404", description = "Notificação não encontrada no banco de dados (Resource Not Found)")
    })
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable String id){
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @GetMapping
    @Operation(summary = "Lista as notificações do cliente autenticado", description = "Busca todo o histórico de notificações pertencentes ao cliente que realizou a requisição, baseando-se no Token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de notificações retornada com sucesso (Pode retornar uma lista vazia)"),
            @ApiResponse(responseCode = "401", description = "Não autorizado: Token JWT ausente, inválido ou expirado")
    })
    public ResponseEntity<List<NotificationResponseDTO>> getMyNotifications() {
        return ResponseEntity.ok(notificationService.getMyNotifications());
    }
}
