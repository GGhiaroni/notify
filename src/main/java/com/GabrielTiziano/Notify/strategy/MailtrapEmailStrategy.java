package com.GabrielTiziano.Notify.strategy;

import com.GabrielTiziano.Notify.client.MailtrapClient;
import com.GabrielTiziano.Notify.dto.MailtrapRequestDTO;
import com.GabrielTiziano.Notify.mapper.MailtrapMapper;
import com.GabrielTiziano.Notify.model.NotificationModel;
import com.GabrielTiziano.Notify.model.enums.NotificationChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailtrapEmailStrategy implements NotificationStrategy{

    @Value("${api.external.mailtrap.token}")
    private String mailtrapToken;

    @Value("${api.external.mailtrap.inbox-id}")
    private String inboxId;

    private final MailtrapClient mailtrapClient;

    @Override
    public boolean suports(NotificationChannel notificationChannel) {
        return NotificationChannel.EMAIL.equals(notificationChannel);
    }

    @Override
    public void send(NotificationModel notificationModel) {
        MailtrapRequestDTO mailtrapRequestDTO = MailtrapMapper.toMailtrapRequestDTO(notificationModel);
        String headerAuthorization = "Bearer " + mailtrapToken;
        mailtrapClient.sendEmail(headerAuthorization, inboxId, mailtrapRequestDTO);
    }
}
