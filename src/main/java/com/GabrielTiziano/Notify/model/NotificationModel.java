package com.GabrielTiziano.Notify.model;

import com.GabrielTiziano.Notify.model.enums.NotificationChannel;
import com.GabrielTiziano.Notify.model.enums.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class NotificationModel {

    @Id
    private String id;

    private String clientId;

    private String recipient;
    private String subject;
    private String body;

    private NotificationChannel channel;
    private NotificationStatus status;

    private String errorMessage;

    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}
