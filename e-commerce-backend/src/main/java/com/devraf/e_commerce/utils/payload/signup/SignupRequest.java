package com.devraf.e_commerce.utils.payload.signup;

import com.devraf.e_commerce.utils.annotations.ConfirmPassword;
import com.devraf.e_commerce.utils.annotations.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
@ConfirmPassword
public class SignupRequest implements Serializable {
    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @Password
    private String password;

    private String confirmationPassword;
}
