package com.devraf.e_commerce.payload.password;

import com.devraf.e_commerce.utils.annotations.ConfirmPassword;
import com.devraf.e_commerce.utils.annotations.Password;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfirmPassword
public class ResetPasswordRequest {
    @NotNull
    @NotBlank
    @Password
    private String password;

    @NotNull
    @NotBlank
    private String confirmPassword;

    @NotBlank
    @NotNull
    @Size(max = 256)
    private String token;
}
