package ru.shtyrev.calculator_service.services;

import ru.shtyrev.calculator_service.dtos.CreditDto;
import ru.shtyrev.calculator_service.dtos.LoanOfferDto;
import ru.shtyrev.calculator_service.dtos.LoanStatementRequestDto;
import ru.shtyrev.calculator_service.dtos.ScoringDataDto;

import java.util.List;

public interface CalculatorService {
    List<LoanOfferDto> offers(LoanStatementRequestDto loanStatementRequestDto);

    CreditDto calc(ScoringDataDto scoringDataDto);
}
