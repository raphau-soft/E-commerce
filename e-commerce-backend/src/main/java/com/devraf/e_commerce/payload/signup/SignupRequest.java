package com.devraf.e_commerce.payload.signup;

import com.devraf.e_commerce.annotations.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @Password
    private String password;

    private String confirmationPassword;
}
