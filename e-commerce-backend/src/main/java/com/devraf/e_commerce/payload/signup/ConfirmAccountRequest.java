package com.devraf.e_commerce.payload.signup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class ConfirmAccountRequest implements Serializable {
    @NotBlank
    @NotNull
    @Size(min = 3, max = 30)
    private String username;

    @NotBlank
    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @NotBlank
    @NotNull
    @Size(min = 2, max = 50)
    private String surname;

    @NotBlank
    @NotNull
    @Pattern(regexp = "^\\+[0-9]{2} [0-9]{3} [0-9]{3} [0-9]{3}$", message = "phoneNumber should be valid")
    private String phoneNumber;

    @NotBlank
    @NotNull
    @Size(max = 256)
    private String token;
}
