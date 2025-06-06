package com.backend.fambien.dto;

/**
 * Response body for authentication.
 * Contains the generated JWT token after successful login.
 */
public record AuthResponse (String token) {}
