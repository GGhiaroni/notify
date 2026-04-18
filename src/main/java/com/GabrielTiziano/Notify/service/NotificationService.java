package com.GabrielTiziano.Notify.service;

import com.GabrielTiziano.Notify.client.MailtrapClient;
import com.GabrielTiziano.Notify.dto.MailtrapRequestDTO;
import com.GabrielTiziano.Notify.dto.NotificationRequestDTO;
import com.GabrielTiziano.Notify.dto.NotificationResponseDTO;
import com.GabrielTiziano.Notify.mapper.MailtrapMapper;
import com.GabrielTiziano.Notify.mapper.NotificationMapper;
import com.GabrielTiziano.Notify.model.NotificationModel;
import com.GabrielTiziano.Notify.model.enums.NotificationStatus;
import com.GabrielTiziano.Notify.repository.NotificationRepository;
import com.GabrielTiziano.Notify.security.JWTUserData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Value("${api.external.mailtrap.token}")
    private String mailtrapToken;

    @Value("${api.external.mailtrap.inbox-id}")
    private String inboxId;

    private final NotificationRepository notificationRepository;
    private final MailtrapClient mailtrapClient;

    public NotificationResponseDTO sendNotification(NotificationRequestDTO notificationRequestDTO){
        JWTUserData userPrincipal = (JWTUserData)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        NotificationModel notificationModel = NotificationMapper.toModel(notificationRequestDTO);

        notificationModel.setClientId(userPrincipal.id());

        notificationModel = notificationRepository.save(notificationModel);

        try {
            sendEmailThroughMailTrap(notificationModel);
            notificationModel.setStatus(NotificationStatus.SENT);
        } catch (Exception e) {
            notificationModel.setStatus(NotificationStatus.FAILED);
        }

        return NotificationMapper.toNotificationResponseDTO(notificationRepository.save(notificationModel));
    }

    private void sendEmailThroughMailTrap(NotificationModel notificationModel){
        MailtrapRequestDTO mailtrapRequestDTO = MailtrapMapper.toMailtrapRequestDTO(notificationModel);

        String headerAuthorization = "Bearer " + mailtrapToken;

        mailtrapClient.sendEmail(headerAuthorization, inboxId, mailtrapRequestDTO);
    }
}
