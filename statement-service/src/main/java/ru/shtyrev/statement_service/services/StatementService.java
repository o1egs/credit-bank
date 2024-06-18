package ru.shtyrev.statement_service.services;

import ru.shtyrev.dtos.dtos.LoanOfferDto;
import ru.shtyrev.dtos.dtos.LoanStatementRequestDto;

import java.util.List;

public interface StatementService {
    List<LoanOfferDto> createStatement(LoanStatementRequestDto loanStatementRequestDto);

    void selectOffer(LoanOfferDto loanOfferDto);
}
