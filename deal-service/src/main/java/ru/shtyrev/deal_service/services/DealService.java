package ru.shtyrev.deal_service.services;

import ru.shtyrev.dtos.dtos.CreditDto;
import ru.shtyrev.dtos.dtos.FinishRegistrationRequestDto;
import ru.shtyrev.dtos.dtos.LoanOfferDto;
import ru.shtyrev.dtos.dtos.LoanStatementRequestDto;

import java.util.List;

public interface DealService {
    List<LoanOfferDto> createStatement(LoanStatementRequestDto loanStatementRequestDto);

    void selectOffer(LoanOfferDto loanOfferDto);

    CreditDto calculate(String statementId, FinishRegistrationRequestDto finishRegistrationRequestDto);
}
