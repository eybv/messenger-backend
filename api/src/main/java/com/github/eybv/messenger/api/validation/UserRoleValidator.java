package com.github.eybv.messenger.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UserRoleValidator implements ConstraintValidator<UserRole, String> {

    private final List<String> roles = List.of("user", "admin");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && roles.contains(value.toLowerCase());
    }

}
