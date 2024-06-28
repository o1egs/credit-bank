package ru.shtyrev.statement_service.exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionResponse {
    HttpStatus httpStatus;
    String message;
}
