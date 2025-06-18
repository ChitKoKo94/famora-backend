package com.backend.fambien.utility;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public final class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expirationMilliSec;
    private static Key key;

    /**
     * Initializes the signing key from the secret string.
     * This method is called after dependency injection is complete.
     */
    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
        //logger.info("JWTUtil initialized. Secret key loaded.");
    }

    /**
     * Extracts a specific claim from the token.
     *
     * @param token The JWT token.
     * @param claimsResolver A function to resolve the desired claim from the token's claims.
     * @param <T> The type of the claim.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the token.
     *
     * @param token The JWT token.
     * @return All claims contained within the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts the username from the token.
     *
     * @param token The JWT token.
     * @return The username.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the token.
     *
     * @param token The JWT token.
     * @return The expiration date.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Checks if the token is expired.
     *
     * @param token The JWT token.
     * @return True if the token is expired, false otherwise.
     */
    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        }
        catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * Creates the JWT token with username, and expiration.
     *
     * @param username The subject of the token (usually the username).
     * @return The created JWT token.
     */
    public String generateToken(String username) {
        Date now = new Date(); ;
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMilliSec))
                .signWith(key)
                .compact();
    }

    /**
     * Creates the Refresh JWT token with username, and expiration.
     *
     * @param username The subject of the token (usually the username).
     * @return The Refresh JWT token.
     */
    public String generateRefreshToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + (expirationMilliSec * 2)))
                .signWith(key)
                .compact();
    }
}
