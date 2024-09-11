package com.devraf.e_commerce.utils.exception;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class ExceptionResponse {
    private int status;
    private String error;
    private List<String> message;
    private OffsetDateTime timestamp;
}
