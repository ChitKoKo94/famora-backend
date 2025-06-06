package com.backend.fambien.dto;

/**
 * Request body for authentication.
 * Used to capture username and password during login.
 */
public record AuthRequest(String username,
                          String password) {
}
