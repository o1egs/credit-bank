package ru.shtyrev.statement_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class StatementExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> messages = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(er -> {
            messages.put(er.getField(), er.getDefaultMessage());
        });

        ValidationExceptionResponse exceptionResponse = ValidationExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .messages(messages)
                .build();

        return ResponseEntity.badRequest()
                .body(exceptionResponse);
    }

}
