package com.moba.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
