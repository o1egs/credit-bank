package ru.shtyrev.deal_service.controllers;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shtyrev.deal_service.services.DealService;
import ru.shtyrev.dtos.dtos.CreditDto;
import ru.shtyrev.dtos.dtos.FinishRegistrationRequestDto;
import ru.shtyrev.dtos.dtos.LoanOfferDto;
import ru.shtyrev.dtos.dtos.LoanStatementRequestDto;

import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DealController {
    DealService dealService;

    @PostMapping("/statement")
    ResponseEntity<List<LoanOfferDto>> createStatement(@RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        List<LoanOfferDto> loanOfferDtos = dealService.createStatement(loanStatementRequestDto);
        return ResponseEntity.ok(loanOfferDtos);
    }

    @PostMapping("/offer/select")
    ResponseEntity<?> selectOffer(@RequestBody LoanOfferDto loanOfferDto) {
        dealService.selectOffer(loanOfferDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calculate/{statementId}")
    ResponseEntity<CreditDto> calculate(@PathVariable String statementId, @RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto) {
        CreditDto creditDto = dealService.calculate(statementId, finishRegistrationRequestDto);
        return ResponseEntity.ok(creditDto);
    }
}
