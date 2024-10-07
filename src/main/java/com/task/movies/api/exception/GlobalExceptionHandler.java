package com.task.movies.api.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {


    private HashMap<String, String> getErrorMessage(String message) {
        HashMap<String, String> errorMessage = new HashMap<>();
        errorMessage.put("message", message);
        return errorMessage;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<HashMap<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(getErrorMessage(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HashMap<String, String>> handleGlobalException(Exception ex, WebRequest request) {


        return new ResponseEntity<>(getErrorMessage(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
