package ru.shtyrev.calculator_service.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shtyrev.calculator_service.dtos.CreditDto;
import ru.shtyrev.calculator_service.dtos.LoanOfferDto;
import ru.shtyrev.calculator_service.dtos.LoanStatementRequestDto;
import ru.shtyrev.calculator_service.dtos.ScoringDataDto;
import ru.shtyrev.calculator_service.services.CalculatorService;

import java.util.List;

@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CalculatorController {
    CalculatorService calculatorService;

    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> offers(@RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        List<LoanOfferDto> offers = calculatorService.offers(loanStatementRequestDto);
        return ResponseEntity.ok(offers);
    }

    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calc(@RequestBody ScoringDataDto scoringDataDto) {
        CreditDto calc = calculatorService.calc(scoringDataDto);
        return ResponseEntity.ok(calc);
    }
}
