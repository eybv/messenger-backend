package com.github.eybv.messenger.api.request;

import com.github.eybv.messenger.api.validation.UUID;
import com.github.eybv.messenger.application.data.UserData;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UpdateAccountRequest {

    @UUID
    private String id;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email should not be blank")
    private String email;

    @NotBlank(message = "Firstname should not be blank")
    @Pattern(regexp = "\\p{L}{2,}", message = "Firstname should contain at least 2 letters without numbers and symbols")
    private String firstname;

    @NotBlank(message = "Lastname should not be blank")
    @Pattern(regexp = "\\p{L}{2,}", message = "Lastname should contain at least 2 letters without numbers and symbols")
    private String lastname;

    @Pattern(regexp = "\\p{L}*", message = "Patronymic (if exists) should contain at least 2 letters without numbers and symbols")
    private String patronymic;

    public UserData toUserData() {
        return UserData.builder()
                .id(id)
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .patronymic(patronymic)
                .build();
    }

}
