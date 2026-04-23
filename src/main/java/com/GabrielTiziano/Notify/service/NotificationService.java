package com.GabrielTiziano.Notify.service;

import com.GabrielTiziano.Notify.dto.NotificationRequestDTO;
import com.GabrielTiziano.Notify.dto.NotificationResponseDTO;
import com.GabrielTiziano.Notify.exception.ResourceNotFoundException;
import com.GabrielTiziano.Notify.mapper.NotificationMapper;
import com.GabrielTiziano.Notify.model.NotificationModel;
import com.GabrielTiziano.Notify.model.enums.NotificationStatus;
import com.GabrielTiziano.Notify.repository.NotificationRepository;
import com.GabrielTiziano.Notify.security.JWTUserData;
import com.GabrielTiziano.Notify.strategy.NotificationStrategy;
import com.GabrielTiziano.Notify.strategy.NotificationStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationStrategyFactory notificationStrategyFactory;

    public NotificationResponseDTO sendNotification(NotificationRequestDTO notificationRequestDTO){
        JWTUserData userPrincipal = (JWTUserData)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        NotificationModel notificationModel = NotificationMapper.toModel(notificationRequestDTO);

        notificationModel.setClientId(userPrincipal.id());

        notificationModel = notificationRepository.save(notificationModel);

        try {
            NotificationStrategy strategy = notificationStrategyFactory.getStrategy(notificationRequestDTO.channel());
            strategy.send(notificationModel);
            notificationModel.setStatus(NotificationStatus.SENT);
        } catch (Exception e) {
            notificationModel.setStatus(NotificationStatus.FAILED);
        }

        return NotificationMapper.toNotificationResponseDTO(notificationRepository.save(notificationModel));
    }

    public NotificationResponseDTO getNotificationById(String id){
        NotificationModel model = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificação com ID " + id + " não foi encontrada."));

        return NotificationMapper.toNotificationResponseDTO(model);
    }

    public List<NotificationResponseDTO> getMyNotifications() {
        JWTUserData userPrincipal = (JWTUserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentClientId = userPrincipal.id();

        List<NotificationModel> notifications = notificationRepository.findAllByClientId(currentClientId);

        return notifications.stream()
                .map(NotificationMapper::toNotificationResponseDTO)
                .toList();
    }
}
