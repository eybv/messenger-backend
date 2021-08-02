package com.github.eybv.messenger;

import com.github.eybv.messenger.application.data.DepartmentData;
import com.github.eybv.messenger.application.data.UserData;
import com.github.eybv.messenger.application.service.department.DepartmentService;
import com.github.eybv.messenger.application.service.user.UserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    @Value("${security.admin-email}")
    private String adminEmail;

    @Value("${security.admin-password}")
    private String adminPassword;

    private final UserService userService;

    private final DepartmentService departmentService;

    public DatabaseInitializer(UserService userService, DepartmentService departmentService) {
        this.userService = userService;
        this.departmentService = departmentService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userService.findAll(null, null, 1, 0).isEmpty()) {
            DepartmentData department = DepartmentData.builder()
                    .name("Ministry of Truth")
                    .build();

            department = departmentService.createDepartment(department);

            UserData user = UserData.builder()
                    .email(adminEmail)
                    .password(adminPassword)
                    .lastname("Blair")
                    .firstname("Eric")
                    .patronymic("Arthur")
                    .build();

            user = userService.createUser(user);

            userService.changeUserRole(user.getId(), "admin");
            userService.changeUserDepartment(user.getId(), department.getId());
        }
    }

}
