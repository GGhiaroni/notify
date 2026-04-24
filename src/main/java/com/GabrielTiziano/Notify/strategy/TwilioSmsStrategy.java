package com.GabrielTiziano.Notify.strategy;

import com.GabrielTiziano.Notify.model.NotificationModel;
import com.GabrielTiziano.Notify.model.enums.NotificationChannel;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TwilioSmsStrategy implements NotificationStrategy{
    @Value("${api.external.twilio.account-sid}")
    private String accountSid;

    @Value("${api.external.twilio.auth-token}")
    private String authToken;

    @Value("${api.external.twilio.phone-number}")
    private String twilioPhoneNumber;

    @PostConstruct
    public void initTwilio(){
        Twilio.init(accountSid, authToken);
    }

    @Override
    public boolean suports(NotificationChannel notificationChannel) {
        return NotificationChannel.SMS.equals(notificationChannel);
    }

    @Override
    public void send(NotificationModel notificationModel) {
        Message.creator(
                new PhoneNumber(notificationModel.getRecipient()),
                new PhoneNumber(twilioPhoneNumber),
                notificationModel.getBody()
        ).create();
    }
}
