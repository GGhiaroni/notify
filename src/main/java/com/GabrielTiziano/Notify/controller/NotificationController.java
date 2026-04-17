package com.GabrielTiziano.Notify.controller;

import com.GabrielTiziano.Notify.dto.NotificationRequestDTO;
import com.GabrielTiziano.Notify.dto.NotificationResponseDTO;
import com.GabrielTiziano.Notify.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notify/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<NotificationResponseDTO> sendNotification(@RequestBody NotificationRequestDTO notificationRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.sendNotification(notificationRequestDTO));
    }
}
