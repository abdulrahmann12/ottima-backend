package com.ottima.finishing_tracking.security;

import com.ottima.finishing_tracking.exception.UserNotActiveException;
import com.ottima.finishing_tracking.exception.UserNotFoundException;
import com.ottima.finishing_tracking.user.entity.User;
import com.ottima.finishing_tracking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmailOrPhoneNumberWithRole(username)
                .orElseThrow(UserNotFoundException::new);

        if (!user.isActive()) {
            throw new UserNotActiveException();
        }

        return new CustomUserDetails(user);
    }
}
