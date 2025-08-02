package io.chatplatform.messaging.user.security.detail;

import io.chatplatform.messaging.user.repository.UserRepository;
import io.chatplatform.messaging.user.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(SecurityUser::from)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
    }
}
