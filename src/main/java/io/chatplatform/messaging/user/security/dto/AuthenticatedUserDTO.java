package io.chatplatform.messaging.user.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatedUserDTO implements Principal {
    private String userId;
    private String username;

    @Override
    public String getName() {
        return userId;
    }
}