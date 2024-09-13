package com.devraf.e_commerce.utils.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {InternalAuthenticationServiceException.class, UserNotActiveException.class, BadCredentialsException.class})
    protected ResponseEntity<Object> handleAuthenticationException(Exception e, WebRequest request) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(List.of("Invalid email or password!"))
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.name())
                .timestamp(OffsetDateTime.now())
                .build();
        return handleExceptionInternal(e, response,
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = {TokenNotValidException.class})
    protected ResponseEntity<Object> handleAuthenticationException(TokenNotValidException e, WebRequest request) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(List.of("Token is invalid"))
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.name())
                .timestamp(OffsetDateTime.now())
                .build();
        return handleExceptionInternal(e, response,
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(new ArrayList<>())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(OffsetDateTime.now())
                .build();
        for(ObjectError objectError: ex.getBindingResult().getAllErrors()) {
            if(objectError instanceof FieldError fieldError) {
                response.getMessage().add(fieldError.getField() + " - " + fieldError.getDefaultMessage());
            } else {
                response.getMessage().add(objectError.getDefaultMessage());
            }
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
