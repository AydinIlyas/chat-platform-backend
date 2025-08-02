package io.chatplatform.messaging.user.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenClaims {
    private String userId;
    private String username;
}
