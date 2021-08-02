package com.github.eybv.messenger.application.data;

import com.github.eybv.messenger.core.department.Department;
import com.github.eybv.messenger.core.user.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserData {

    private String id;
    private String role;
    private String email;
    private String firstname;
    private String lastname;
    private String patronymic;
    private String department;
    private boolean enabled;

    private transient String password;

    public static UserData fromEntity(User user) {
        UserDataBuilder userDataBuilder = UserData.builder();

        userDataBuilder
                .id(user.getId().toString())
                .role(user.getRole().name().toLowerCase())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .enabled(user.isEnabled());

        user.getPatronymic()
                .ifPresent(userDataBuilder::patronymic);

        user.getDepartment()
                .map(Department::getName)
                .ifPresent(userDataBuilder::department);

        return userDataBuilder.build();
    }

}
