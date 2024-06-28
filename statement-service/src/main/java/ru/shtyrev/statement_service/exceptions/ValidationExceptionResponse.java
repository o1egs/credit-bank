package ru.shtyrev.statement_service.exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@Data
@Builder
public class ValidationExceptionResponse {
    HttpStatus httpStatus;
    Map<String, String> messages;
}
