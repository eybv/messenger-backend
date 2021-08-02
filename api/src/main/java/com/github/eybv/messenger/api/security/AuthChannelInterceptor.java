package com.github.eybv.messenger.api.security;

import com.github.eybv.messenger.api.exception.AuthenticationFailureException;
import com.github.eybv.messenger.application.data.UserData;
import com.github.eybv.messenger.application.security.JwtProvider;
import com.github.eybv.messenger.application.service.user.UserService;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final JwtProvider jwtProvider;

    private final UserService userService;

    public AuthChannelInterceptor(JwtProvider jwtProvider, UserService userService) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor.getCommand() == StompCommand.CONNECT) {
            Optional.ofNullable(accessor.getFirstNativeHeader("Authorization"))
                    .map(token -> token.substring(7))
                    .flatMap(jwtProvider::getSubjectFromToken)
                    .flatMap(userService::findById)
                    .filter(UserData::isEnabled)
                    .orElseThrow(AuthenticationFailureException::new);
        }

        return message;
    }

}
