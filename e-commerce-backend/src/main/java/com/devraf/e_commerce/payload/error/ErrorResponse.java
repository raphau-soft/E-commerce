package com.devraf.e_commerce.payload.error;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ErrorResponse {
    HttpStatus httpStatus;
    String message;
}
