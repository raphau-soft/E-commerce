package com.devraf.e_commerce.utils.payload.signup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class ConfirmAccountRequest implements Serializable {
    @NotBlank
    @NotNull
    @Size(max = 100)
    private String username;

    @NotBlank
    @NotNull
    @Size(max = 100)
    private String name;

    @NotBlank
    @NotNull
    @Size(max = 100)
    private String surname;

    @NotBlank
    @NotNull
    private String phoneNumber;

    @NotBlank
    @NotNull
    private String token;
}
