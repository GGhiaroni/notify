package com.GabrielTiziano.Notify.security;

import com.GabrielTiziano.Notify.model.ClientAppModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Component
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private Instant generationExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String generateToken(ClientAppModel clientAppModel) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withSubject(clientAppModel.getEmail())
                    .withClaim("userId", clientAppModel.getId())
                    .withClaim("name", clientAppModel.getClientName())
                    .withExpiresAt(generationExpirationDate())
                    .withIssuedAt(Instant.now())
                    .withIssuer("API Notify")
                    .sign(algorithm);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<JWTUserData> verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);

            return Optional.of(JWTUserData.builder()
                    .id(jwt.getClaim("userId").asString())
                    .name(jwt.getClaim("name").asString())
                    .email(jwt.getSubject())
                    .build());

        } catch (RuntimeException e) {
            return Optional.empty();
        }

    }
}
