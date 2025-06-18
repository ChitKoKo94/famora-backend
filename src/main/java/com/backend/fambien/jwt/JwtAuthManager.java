package com.backend.fambien.jwt;

import com.backend.fambien.utility.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthManager implements ReactiveAuthenticationManager {
    private final ReactiveUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    /**
     * Authenticates the provided Authentication object.
     * For JWT, this typically means validating the token and loading user details.
     *
     * @param authentication The authentication request object (e.g., UsernamePasswordAuthenticationToken).
     * @return A Mono emitting an authenticated Authentication object.
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username;
        try {
            username = jwtUtil.extractUsername(authToken);
            if (StringUtils.isBlank(username)) {
                return Mono.empty();
            }
        } catch (MalformedJwtException
                 | ExpiredJwtException
                 | UnsupportedJwtException
                 | IllegalArgumentException e) {
            return Mono.error(e);
        }
        return userDetailsService.findByUsername(username)
                .switchIfEmpty(Mono.defer(Mono::empty))
                .map(userDetails -> (Authentication) new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()))
                .onErrorResume(e -> Mono.empty());
    }
}
