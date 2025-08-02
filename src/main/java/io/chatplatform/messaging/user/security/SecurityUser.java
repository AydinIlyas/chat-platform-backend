package io.chatplatform.messaging.user.security;

import io.chatplatform.messaging.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Adapter that turns a domain {@link User} into Spring-Security {@link UserDetails}.
 */
public final class SecurityUser implements UserDetails {

    private final String id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;


    public static SecurityUser from(User u) {
        return new SecurityUser(
                u.getId(),
                u.getUsername(),
                u.getPassword(),
                List.of()
        );
    }

    private SecurityUser(String id,
                         String username,
                         String password,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
