package com.GabrielTiziano.Notify.repository;

import com.GabrielTiziano.Notify.model.NotificationModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<NotificationModel, String> {
    List<NotificationModel> findAllByClientId(String clientId);
}
