package ru.shtyrev.calculator_service.services;

import ru.shtyrev.calculator_service.dtos.CreditDto;
import ru.shtyrev.calculator_service.dtos.LoanOfferDto;

import java.util.List;

public interface CalculatorService {
    List<LoanOfferDto> offers();

    CreditDto calc();
}
