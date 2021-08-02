package com.github.eybv.messenger.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserRoleValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserRole {
    String message() default "Invalid user role";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
