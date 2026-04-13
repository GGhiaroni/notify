package com.GabrielTiziano.Notify.repository;

import com.GabrielTiziano.Notify.model.NotificationModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<NotificationModel, String> {
}
