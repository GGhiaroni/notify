package com.GabrielTiziano.Notify.strategy;

import com.GabrielTiziano.Notify.model.NotificationModel;
import com.GabrielTiziano.Notify.model.enums.NotificationChannel;

public interface NotificationStrategy {
    boolean suports(NotificationChannel notificationChannel);
    void send(NotificationModel notificationModel);
}
