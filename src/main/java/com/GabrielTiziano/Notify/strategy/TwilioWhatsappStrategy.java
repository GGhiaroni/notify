package com.GabrielTiziano.Notify.strategy;

import com.GabrielTiziano.Notify.model.NotificationModel;
import com.GabrielTiziano.Notify.model.enums.NotificationChannel;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TwilioWhatsappStrategy implements NotificationStrategy{
    @Value("${api.external.twilio.whatsapp-phone-number}")
    private String fromTwilioWhatsAppNumber;

    @Override
    public boolean suports(NotificationChannel notificationChannel) {
        return NotificationChannel.WHATSAPP.equals(notificationChannel);
    }

    @Override
    public void send(NotificationModel notificationModel) {
        log.info("Iniciando envio de WhatsApp para: {}", notificationModel.getRecipient());

        PhoneNumber to = new PhoneNumber("whatsapp:" + notificationModel.getRecipient());
        PhoneNumber from = new PhoneNumber("whatsapp:" + fromTwilioWhatsAppNumber);

        Message message = Message.creator(
                to,
                from,
                notificationModel.getBody()
        ).create();

        log.info("WhatsApp enviado com sucesso! SID da mensagem: {}", message.getSid());
    }
}
