package com.devraf.e_commerce.payload.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @NotBlank
    @Size(max = 100)
    private String password;
}
