package com.devraf.e_commerce.exception;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class ExceptionResponse {
    private OffsetDateTime timestamp;
    private int status;
    private String error;
    private List<String> message;
    private String path;
}
