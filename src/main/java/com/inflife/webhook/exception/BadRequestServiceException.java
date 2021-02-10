package com.inflife.webhook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author mark ortiz
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestServiceException extends Exception {

    public BadRequestServiceException(Throwable e, String message) {
        super(message, e);
    }

    public BadRequestServiceException(String message) {
        super(message);
    }

}