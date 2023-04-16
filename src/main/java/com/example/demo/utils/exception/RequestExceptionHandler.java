package com.example.demo.utils.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

import static com.example.demo.utils.exception.ExceptionUtil.getErrorMessageFromClientException;

@RestControllerAdvice
public class RequestExceptionHandler {

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<String> handleMyCustomException(RestClientResponseException e) {
        return ResponseEntity.status(e.getStatusCode()).body(getErrorMessageFromClientException(e));
    }
}
