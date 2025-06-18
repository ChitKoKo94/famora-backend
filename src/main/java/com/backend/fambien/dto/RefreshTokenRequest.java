package com.backend.fambien.dto;

/**
 * Request body for authentication refresh.
 * Used to capture refresh token to issue new access token
 */
public record RefreshTokenRequest(String refreshToken) {
}