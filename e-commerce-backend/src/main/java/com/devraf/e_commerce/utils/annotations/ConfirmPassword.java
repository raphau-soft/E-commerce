package com.devraf.e_commerce.utils.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Constraint(validatedBy = PasswordConfirmValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfirmPassword {
    String message() default "The passwords do not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
