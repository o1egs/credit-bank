package ru.shtyrev.statement_service.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shtyrev.dtos.dtos.LoanOfferDto;
import ru.shtyrev.dtos.dtos.LoanStatementRequestDto;
import ru.shtyrev.statement_service.services.StatementService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatementController {
    StatementService statementService;

    @PostMapping
    ResponseEntity<List<LoanOfferDto>> createStatement(@Valid @RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        List<LoanOfferDto> loanOfferDtos = statementService.createStatement(loanStatementRequestDto);
        return ResponseEntity.ok(loanOfferDtos);
    }

    @PostMapping("/offer")
    ResponseEntity<?> selectOffer(@RequestBody LoanOfferDto loanOfferDto) {
        statementService.selectOffer(loanOfferDto);
        return ResponseEntity.ok().build();
    }
}
