package com.GabrielTiziano.Notify.mapper;

import com.GabrielTiziano.Notify.dto.MailtrapAddressDTO;
import com.GabrielTiziano.Notify.dto.MailtrapRequestDTO;
import com.GabrielTiziano.Notify.model.NotificationModel;

import java.util.List;

public class MailtrapMapper {
    public static MailtrapRequestDTO toMailtrapRequestDTO(NotificationModel notificationModel){

        MailtrapAddressDTO from = new MailtrapAddressDTO("hello@demomailtrap.com", "API Notify");

        MailtrapAddressDTO to = new MailtrapAddressDTO(notificationModel.getRecipient(), null);

        return new MailtrapRequestDTO(
                from,
                List.of(to),
                notificationModel.getSubject(),
                notificationModel.getBody()

        );
    }
}
