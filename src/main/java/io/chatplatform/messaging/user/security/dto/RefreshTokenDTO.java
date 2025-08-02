package io.chatplatform.messaging.user.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class RefreshTokenDTO {
    private String refreshToken;
    private Instant expiresAt;
}
