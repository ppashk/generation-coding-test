package com.example.demo.utils.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

import static com.example.demo.utils.exception.ExceptionUtil.getErrorMessageForNotReadableException;
import static com.example.demo.utils.exception.ExceptionUtil.getErrorMessageFromClientException;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class RequestExceptionHandler {

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<String> handleRestClientResponseException(RestClientResponseException e) {
        return ResponseEntity.status(e.getStatusCode()).body(getErrorMessageFromClientException(e));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(BAD_REQUEST).body(getErrorMessageForNotReadableException());
    }
}
