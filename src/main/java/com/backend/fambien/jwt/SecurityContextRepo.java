package com.backend.fambien.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityContextRepo implements ServerSecurityContextRepository {
    private final JwtAuthManager authenticationManager;

    /**
     * Saves the SecurityContext (not used for stateless JWT).
     */
    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    /**
     * Loads the SecurityContext from the request.
     * It extracts the JWT from the Authorization header, creates an unauthenticated token,
     * and then uses the JwtAuthenticationManager to authenticate it.
     *
     * @param exchange The current server web exchange.
     * @return A Mono emitting the SecurityContext if authentication is successful, or empty if not.
     */
    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String authToken = authHeader.substring(7);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
            return authenticationManager.authenticate(auth)
                    .map(authentication -> (SecurityContext) new SecurityContextImpl(authentication))
                    .onErrorResume(e -> {
                        log.error(e.getMessage(), e);
                        return Mono.empty();
                    });
        }
        return Mono.empty();
    }
}
