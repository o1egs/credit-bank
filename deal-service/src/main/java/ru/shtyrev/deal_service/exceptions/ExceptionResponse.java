package ru.shtyrev.deal_service.exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ExceptionResponse {
    HttpStatus httpStatus;
    String message;
}
