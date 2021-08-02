package com.github.eybv.messenger.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Password should not be blank")
    @Size(min = 6, message = "Password should not be less 6 symbols")
    private String password;

}
