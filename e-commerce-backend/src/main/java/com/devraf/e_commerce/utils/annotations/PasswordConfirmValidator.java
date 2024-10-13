package com.devraf.e_commerce.utils.annotations;

import com.devraf.e_commerce.payload.signup.SignupRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class PasswordConfirmValidator implements ConstraintValidator<ConfirmPassword, Object> {

    private String message;

    @Override
    public void initialize(ConfirmPassword constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field passwordField = value.getClass().getDeclaredField("password");
            Field confirmPasswordField = value.getClass().getDeclaredField("confirmPassword");

            passwordField.setAccessible(true);
            confirmPasswordField.setAccessible(true);

            Object password = passwordField.get(value);
            Object confirmPassword = confirmPasswordField.get(value);

            boolean isValid = password.equals(confirmPassword);

            if (!isValid) {
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode("confirmPassword")
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            }

            return isValid;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
