package com.github.eybv.messenger.application.service.auth;

import com.github.eybv.messenger.application.data.AccessToken;
import com.github.eybv.messenger.application.security.JwtProvider;
import com.github.eybv.messenger.application.security.PasswordEncoder;
import com.github.eybv.messenger.core.user.User;
import com.github.eybv.messenger.core.user.UserRepository;

import java.util.Map;
import java.util.Optional;

public class DefaultAuthenticationService implements AuthenticationService {

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public DefaultAuthenticationService(JwtProvider jwtProvider,
                                        UserRepository userRepository,
                                        PasswordEncoder passwordEncoder) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<AccessToken> authenticate(String email, String password) {
        return userRepository
                .findByEmail(email)
                .filter(User::isEnabled)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    String subject = user.getId().toString();
                    String token = jwtProvider.createToken(subject, Map.of());
                    return new AccessToken(token);
                });
    }

}
