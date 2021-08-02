package com.github.eybv.messenger.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UUIDValidator implements ConstraintValidator<UUID, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String pattern = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}";
        return value != null && value.toLowerCase().matches(pattern);
    }

}
