package io.chatplatform.messaging.user.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class AccessTokenDTO {
    private String accessToken;
    private String userId;
    private Instant expiresAt;
}
