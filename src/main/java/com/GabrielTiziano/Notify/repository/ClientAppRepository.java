package com.GabrielTiziano.Notify.repository;

import com.GabrielTiziano.Notify.model.ClientAppModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface ClientAppRepository extends MongoRepository<ClientAppModel, String> {
    Optional<UserDetails> findUserByEmail(String email);
}
