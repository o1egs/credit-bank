package ru.shtyrev.calculator_service.services;

import ru.shtyrev.dtos.dtos.*;

import java.util.List;

public interface CalculatorService {
    List<LoanOfferDto> offers(LoanStatementRequestDto loanStatementRequestDto);

    CreditDto calc(ScoringDataDto scoringDataDto);
}
