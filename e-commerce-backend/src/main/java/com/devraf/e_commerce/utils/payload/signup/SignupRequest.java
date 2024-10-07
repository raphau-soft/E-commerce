package com.devraf.e_commerce.utils.payload.signup;

import com.devraf.e_commerce.utils.annotations.ConfirmPassword;
import com.devraf.e_commerce.utils.annotations.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
@ConfirmPassword
public class SignupRequest implements Serializable {
    @NotNull
    @NotBlank
    @Email
    @Size(min = 5, max = 254)
    private String email;

    @NotNull
    @NotBlank
    @Password
    private String password;

    @NotNull
    @NotBlank
    private String confirmPassword;
}
