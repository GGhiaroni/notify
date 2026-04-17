package com.GabrielTiziano.Notify.service;

import com.GabrielTiziano.Notify.dto.NotificationRequestDTO;
import com.GabrielTiziano.Notify.dto.NotificationResponseDTO;
import com.GabrielTiziano.Notify.mapper.NotificationMapper;
import com.GabrielTiziano.Notify.model.NotificationModel;
import com.GabrielTiziano.Notify.repository.NotificationRepository;
import com.GabrielTiziano.Notify.security.JWTUserData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationResponseDTO sendNotification(NotificationRequestDTO notificationRequestDTO){
        JWTUserData userPrincipal = (JWTUserData)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        NotificationModel notificationModel = NotificationMapper.toModel(notificationRequestDTO);

        notificationModel.setClientId(userPrincipal.id());

        return NotificationMapper.toNotificationResponseDTO(notificationRepository.save(notificationModel));
    }
}
