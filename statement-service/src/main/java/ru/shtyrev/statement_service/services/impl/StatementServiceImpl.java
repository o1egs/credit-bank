package ru.shtyrev.statement_service.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.shtyrev.dtos.dtos.LoanOfferDto;
import ru.shtyrev.dtos.dtos.LoanStatementRequestDto;
import ru.shtyrev.statement_service.services.StatementService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatementServiceImpl implements StatementService {
    RestTemplate restTemplate;

    @Override
    public List<LoanOfferDto> createStatement(LoanStatementRequestDto loanStatementRequestDto) {
        ResponseEntity<LoanOfferDto[]> response = restTemplate.postForEntity(
                "http://localhost:8081/deal/statement",
                loanStatementRequestDto,
                LoanOfferDto[].class
        );

        assert response.getBody() != null;

        return List.of(response.getBody());
    }

    @Override
    public void selectOffer(LoanOfferDto loanOfferDto) {
        restTemplate.postForLocation(
                "http://localhost:8081/deal/offer/select",
                loanOfferDto);
    }
}
