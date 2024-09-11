package com.devraf.e_commerce.utils.annotations;

import com.devraf.e_commerce.utils.payload.signup.SignupRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConfirmValidator implements ConstraintValidator<ConfirmPassword, SignupRequest> {

    private String message;

    @Override
    public void initialize(ConfirmPassword constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(SignupRequest request, ConstraintValidatorContext context) {
        boolean isValid = request.getPassword().equals(request.getConfirmationPassword());

        if(!isValid) {
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return isValid;
    }
}
