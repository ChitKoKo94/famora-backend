package com.backend.fambien.controller;

import com.backend.fambien.dto.AuthRequest;
import com.backend.fambien.dto.AuthResponse;
import com.backend.fambien.dto.RefreshTokenRequest;
import com.backend.fambien.service.UserAccountService;
import com.backend.fambien.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * REST Controller for authentication endpoints.
 * Handles user login
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final UserAccountService userAccountService;
    private final JwtUtil jwtUtil;

    /**
     * Authenticates the user with provided credentials and returns a JWT if successful.
     *
     * @param authRequest The authentication request containing username and password.
     * @return A Mono emitting a ResponseEntity with the JWT or an error status.
     */
    @PostMapping("/token")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest authRequest) {
        log.info("Login attempt for user: {}", authRequest.username());
        return userAccountService.authenticateUser(authRequest.username(), authRequest.password())
                .flatMap(userDetails ->
                        Mono.just(ResponseEntity.ok(
                                new AuthResponse(
                                        jwtUtil.generateToken(userDetails.getUsername()),
                                        jwtUtil.generateRefreshToken(userDetails.getUsername()),
                                        null)
                        ))
                )
                .onErrorResume(BadCredentialsException.class, e -> {
                    log.warn("Invalid user: {}, {}", authRequest.username(), e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                })
                .onErrorResume(e -> {
                    log.warn("An unexpected error : {}, {}", authRequest.username(), e.getMessage(), e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    /**
     * Authenticates the user with provided refresh-token and returns a JWT if successful.
     *
     * @param request The authentication request containing refresh-token.
     * @return A Mono emitting a ResponseEntity with the new JWT or an error status.
     */
    @PostMapping("/refresh-token")
    public Mono<ResponseEntity<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        log.info("Refreshing expired access token");
        if (jwtUtil.isTokenExpired(request.refreshToken())) {
            log.info("Refreshing expired token");
            return Mono.just(new ResponseEntity<AuthResponse>(
                    new AuthResponse(null,
                            null,
                            "refresh token expired"),
                    HttpStatus.UNAUTHORIZED));
        }
        String username = jwtUtil.extractUsername(request.refreshToken());
        return Mono.just(ResponseEntity.ok(
                new AuthResponse(
                        jwtUtil.generateToken(username),
                        request.refreshToken(),
                        null)
        ));
    }
}
