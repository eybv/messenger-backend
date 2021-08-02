package com.github.eybv.messenger;

import com.github.eybv.messenger.application.security.JwtProvider;
import com.github.eybv.messenger.application.security.PasswordEncoder;
import com.github.eybv.messenger.application.service.auth.AuthenticationService;
import com.github.eybv.messenger.application.service.auth.DefaultAuthenticationService;
import com.github.eybv.messenger.application.service.department.DefaultDepartmentService;
import com.github.eybv.messenger.application.service.department.DepartmentService;
import com.github.eybv.messenger.application.service.messaging.DefaultMessagingService;
import com.github.eybv.messenger.application.service.messaging.MessagingService;
import com.github.eybv.messenger.application.service.user.DefaultUserService;
import com.github.eybv.messenger.application.service.user.UserService;
import com.github.eybv.messenger.application.service.uuid.DefaultUUIDService;
import com.github.eybv.messenger.application.service.uuid.UUIDService;
import com.github.eybv.messenger.core.department.DepartmentRepository;
import com.github.eybv.messenger.core.message.DeletedMessageRepository;
import com.github.eybv.messenger.core.message.MessageRepository;
import com.github.eybv.messenger.core.user.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public UUIDService uuidService() {
        return new DefaultUUIDService();
    }

    @Bean
    public AuthenticationService authenticationService(JwtProvider jwtProvider, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new DefaultAuthenticationService(jwtProvider, userRepository, passwordEncoder);
    }

    @Bean
    public UserService userService(UserRepository userRepository, DepartmentRepository departmentRepository, PasswordEncoder passwordEncoder) {
        return new DefaultUserService(userRepository, departmentRepository, passwordEncoder);
    }

    @Bean
    public DepartmentService departmentService(DepartmentRepository departmentRepository, UserRepository userRepository) {
        return new DefaultDepartmentService(departmentRepository, userRepository);
    }

    @Bean
    public MessagingService messagingService(UUIDService uuidService, UserRepository userRepository, MessageRepository messageRepository, DeletedMessageRepository deletedMessageRepository) {
        return new DefaultMessagingService(uuidService, userRepository, messageRepository, deletedMessageRepository);
    }

}
