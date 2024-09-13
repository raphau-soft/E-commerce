package com.devraf.e_commerce.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserNotActiveException extends RuntimeException {
    public UserNotActiveException() {
        super();
    }

    public UserNotActiveException(String message) {
        super(message);
    }

    public UserNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotActiveException(Throwable cause) {
        super(cause);
    }
}
