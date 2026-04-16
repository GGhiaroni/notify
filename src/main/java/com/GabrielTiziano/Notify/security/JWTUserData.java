package com.GabrielTiziano.Notify.security;

import lombok.Builder;

@Builder
public record JWTUserData(String id, String name, String email) {

}
