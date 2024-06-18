package ru.shtyrev.deal_service.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.shtyrev.deal_service.entities.Client;
import ru.shtyrev.deal_service.entities.Statement;
import ru.shtyrev.deal_service.repositories.ClientRepository;
import ru.shtyrev.deal_service.repositories.CreditRepository;
import ru.shtyrev.deal_service.repositories.StatementRepository;
import ru.shtyrev.dtos.dtos.LoanOfferDto;
import ru.shtyrev.dtos.dtos.LoanStatementRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {
    @Mock
    RestTemplate restTemplate;
    @Mock
    ClientRepository clientRepository;
    @Mock
    StatementRepository statementRepository;
    @Mock
    CreditRepository creditRepository;
    @InjectMocks
    DealServiceImpl dealService;


    @Test
    void createStatement() {
        Client savedClient = Client.builder()
                .id(UUID.randomUUID())
                .build();
        when(clientRepository.save(any())).thenReturn(savedClient);

        Statement savedStatement = Statement.builder()
                .id(UUID.randomUUID())
                .build();
        when(statementRepository.save(any())).thenReturn(savedStatement);

        LoanStatementRequestDto loanStatementRequestDto = new LoanStatementRequestDto();

        LoanOfferDto loanOfferDto = new LoanOfferDto();
        LoanOfferDto [] responseBody
                = { loanOfferDto, loanOfferDto, loanOfferDto, loanOfferDto };
        ResponseEntity<LoanOfferDto[]> response = ResponseEntity.ok(responseBody);

        when(restTemplate.postForEntity(any(), any(), LoanOfferDto[].class))
                .thenReturn(response);


        List<LoanOfferDto> loanOfferDtos = dealService.createStatement(loanStatementRequestDto);

        int expectedSize = 4;
        assertEquals(expectedSize, loanOfferDtos.size());

        UUID expectedId = savedStatement.getId();
        assertEquals(expectedId, loanOfferDtos.get(0).getStatementId());
    }

    @Test
    void selectOffer() {
        Statement foundStatement = Statement.builder()
                .id(UUID.randomUUID())
                .statusHistory(new ArrayList<>())
                .build();
        when(statementRepository.findById(any()))
                .thenReturn(Optional.of(foundStatement));

        int sizeBefore = foundStatement.getStatusHistory().size();

        LoanOfferDto loanOfferDto = new LoanOfferDto();
        loanOfferDto.setStatementId(foundStatement.getId());

        dealService.selectOffer(loanOfferDto);

        int sizeAfter = foundStatement.getStatusHistory().size();
        assertEquals(sizeBefore + 1, sizeAfter);

        LoanOfferDto appliedOffer = foundStatement.getAppliedOffer();
        assertEquals(loanOfferDto, appliedOffer);
    }
}