package io.chatplatform.messaging.user.security.jwt;


import io.chatplatform.messaging.user.security.SecurityUser;
import io.chatplatform.messaging.user.security.dto.AccessTokenDTO;
import io.chatplatform.messaging.user.security.dto.AuthenticatedUserDTO;
import io.chatplatform.messaging.user.security.dto.RefreshTokenDTO;
import io.chatplatform.messaging.user.security.dto.TokenClaims;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Component
@Log4j2
public class JwtUtils {

    @Value("${jwt.secret-key}")
    private String jwtSecret;
    @Value("${jwt.access-token.expiration-ms}")
    private long accessTokenExpirationMs;
    @Value("${jwt.refresh-token.expiration-ms}")
    private long refreshTokenExpirationMs;
    private volatile SecretKey cachedKey;

    public AccessTokenDTO generateAccessToken(TokenClaims tokenClaims) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(accessTokenExpirationMs);

        String accessToken = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(tokenClaims.getUserId())
                .claim("username", tokenClaims.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(getSigningKey())
                .compact();
        return new AccessTokenDTO(accessToken, tokenClaims.getUserId(), expiresAt);
    }

    public RefreshTokenDTO generateRefreshToken(TokenClaims tokenClaims) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(refreshTokenExpirationMs);

        String refreshToken = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(tokenClaims.getUserId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(getSigningKey())
                .compact();
        return new RefreshTokenDTO(refreshToken, expiresAt);
    }

    private SecretKey getSigningKey() {
        if (cachedKey == null) {
            synchronized (this) {
                if (cachedKey == null) {
                    validateSecretKey();
                    cachedKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
                }
            }
        }
        return cachedKey;
    }

    private void validateSecretKey() {
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            throw new IllegalArgumentException("JWT secret key cannot be null or empty. Please check your configuration.");
        }
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String authToken, SecurityUser user) {
        try {
            AuthenticatedUserDTO currentUser = validateToken(authToken);
            return isValidUsername(currentUser.getUsername(), user);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public AuthenticatedUserDTO validateToken(String authToken) {
        JwtParser jwtParser = Jwts.parser().verifyWith(getSigningKey()).build();
        jwtParser.parse(authToken);
        Claims claims = jwtParser.parseSignedClaims(authToken).getPayload();
        return new AuthenticatedUserDTO(claims.getSubject(), claims.get("username", String.class));
    }

    private boolean isValidUsername(String username, SecurityUser authUser) {
        return authUser.getUsername().equals(username);
    }
}