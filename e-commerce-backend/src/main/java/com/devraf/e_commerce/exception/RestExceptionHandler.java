package com.devraf.e_commerce.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = InternalAuthenticationServiceException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ExceptionResponse handleAuthenticationException(RuntimeException e, ServletWebRequest request) {
        return ExceptionResponse.builder()
                .message(List.of("Invalid username or password!"))
                .status(HttpStatus.FORBIDDEN.value())
                .error("Forbidden")
                .timestamp(OffsetDateTime.now())
                .path(request.getRequest().getRequestURI())
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(new ArrayList<>())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .timestamp(OffsetDateTime.now())
                .build();
        for(ObjectError error: ex.getBindingResult().getAllErrors()) {
            FieldError fieldError = (FieldError) error;
            response.getMessage().add(fieldError.getField() + " - " + fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
