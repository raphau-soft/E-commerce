package com.devraf.e_commerce.utils.payload.login;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String csrfToken;
}
