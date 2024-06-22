package ru.shtyrev.deal_service.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.shtyrev.deal_service.entities.Client;
import ru.shtyrev.deal_service.entities.Statement;
import ru.shtyrev.deal_service.jsonbs.Employment;
import ru.shtyrev.deal_service.jsonbs.Passport;
import ru.shtyrev.deal_service.jsonbs.StatusHistory;
import ru.shtyrev.deal_service.repositories.ClientRepository;
import ru.shtyrev.deal_service.repositories.CreditRepository;
import ru.shtyrev.deal_service.repositories.StatementRepository;
import ru.shtyrev.deal_service.services.DealService;
import ru.shtyrev.dtos.dtos.*;
import ru.shtyrev.dtos.enums.ApplicationStatus;
import ru.shtyrev.dtos.enums.ChangeType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DealServiceImpl implements DealService {
    static final Logger logger = LoggerFactory.getLogger(DealServiceImpl.class);

    ClientRepository clientRepository;
    StatementRepository statementRepository;
    CreditRepository creditRepository;

    RestTemplate restTemplate;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<LoanOfferDto> createStatement(LoanStatementRequestDto loanStatementRequestDto) {
        logger.info("Creating passport for {} {}", loanStatementRequestDto.getFirstName(), loanStatementRequestDto.getLastName());
        Passport passport = Passport.builder()
                .number(loanStatementRequestDto.getPassportNumber())
                .series(loanStatementRequestDto.getPassportSeries())
                .build();


        logger.info("Creating client {} {}", loanStatementRequestDto.getFirstName(), loanStatementRequestDto.getLastName());
        Client client = Client.builder()
                .firstName(loanStatementRequestDto.getFirstName())
                .lastName(loanStatementRequestDto.getLastName())
                .middleName(loanStatementRequestDto.getMiddleName())
                .birthdate(loanStatementRequestDto.getBirthdate())
                .email(loanStatementRequestDto.getEmail())
                .passport(passport)
                .build();

        logger.info("Saving client");
        Client savedClient = clientRepository.save(client);
        logger.info("Client saved with id: {}", savedClient.getId().toString());

        logger.info("Creating statement");
        LocalDate now = LocalDate.now();
        StatusHistory statusHistory = StatusHistory.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .time(now)
                .changeType(ChangeType.AUTOMATIC)
                .build();

        Statement statement = Statement.builder()
                .client(savedClient)
                .status(ApplicationStatus.PREAPPROVAL)
                .creationDate(now)
                .statusHistory(List.of(statusHistory))
                .build();

        Statement savedStatement = statementRepository.save(statement);
        logger.info("Statement saved with id: {}", savedStatement.getId().toString());

        logger.info("Sending request");
        ResponseEntity<LoanOfferDto[]> response = restTemplate.postForEntity(
                "http://localhost:8080/calculator/offers",
                loanStatementRequestDto,
                LoanOfferDto[].class);

        assert response.getBody() != null;

        for (LoanOfferDto loanOfferDto : response.getBody()) {
            loanOfferDto.setStatementId(savedStatement.getId());
        }

        return List.of(response.getBody());
    }

    @Override
    public void selectOffer(LoanOfferDto loanOfferDto) {
        logger.info("Try to find statement by id");
        Statement statement = statementRepository.findById(loanOfferDto.getStatementId())
                .orElseThrow();
        logger.info("Statement with id {} found", statement.getId());

        logger.info("Setting status {} to statement with id {}", ApplicationStatus.APPROVED, statement.getId());
        statement.setStatus(ApplicationStatus.APPROVED);
        List<StatusHistory> statusHistory = statement.getStatusHistory();
        StatusHistory historyElement = StatusHistory.builder()
                .status(ApplicationStatus.APPROVED)
                .time(LocalDate.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();
        logger.info("Adding history element to statement with id {}", statement.getId());
        statusHistory.add(historyElement);
        statement.setStatusHistory(statusHistory);

        logger.info("Setting applied offer to statement with id {}", statement.getId());
        statement.setAppliedOffer(loanOfferDto);

        logger.info("Saving statement with id {}", statement.getId());
        statementRepository.save(statement);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CreditDto calculate(String statementId, FinishRegistrationRequestDto finishRegistrationRequestDto) {
        logger.info("Try to find statement by id");
        Statement statement = statementRepository.findById(UUID.fromString(statementId))
                .orElseThrow();
        logger.info("Statement with id {} found", statement.getId());

        Client statementClient = statement.getClient();
        Passport statementClientPassport = statementClient.getPassport();

        logger.info("Creating scoringDataDto");
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(statement.getAppliedOffer().getRequestAmount())
                .term(statement.getAppliedOffer().getTerm())
                .firstName(statementClient.getFirstName())
                .lastName(statementClient.getLastName())
                .middleName(statementClient.getMiddleName())
                .gender(finishRegistrationRequestDto.getGender())
                .birthdate(statementClient.getBirthdate())
                .passportSeries(statementClientPassport.getSeries())
                .passportNumber(statementClientPassport.getNumber())
                .passportIssueDate(finishRegistrationRequestDto.getPassportIssueDate())
                .passportIssueBranch(finishRegistrationRequestDto.getPassportIssueBranch())
                .maritalStatus(finishRegistrationRequestDto.getMaritalStatus())
                .dependentAmount(finishRegistrationRequestDto.getDependentAmount())
                .employment(finishRegistrationRequestDto.getEmployment())
                .accountNumber(finishRegistrationRequestDto.getAccountNumber())
                .isInsuranceEnabled(statement.getAppliedOffer().getIsInsuranceEnabled())
                .isSalaryClient(statement.getAppliedOffer().getIsSalaryClient())
                .build();

        statementClient.setGender(finishRegistrationRequestDto.getGender());
        statementClient.setMaritalStatus(finishRegistrationRequestDto.getMaritalStatus());
        statementClient.setDependentAmount(finishRegistrationRequestDto.getDependentAmount());

        statementClientPassport.setIssueDate(finishRegistrationRequestDto.getPassportIssueDate().toString());
        statementClientPassport.setIssueBranch(finishRegistrationRequestDto.getPassportIssueBranch());

        statementClient.setPassport(statementClientPassport);

        EmploymentDto employmentDto = finishRegistrationRequestDto.getEmployment();
        Employment employment = Employment.builder()
                .salary(employmentDto.getSalary())
                .workExperienceCurrent(employmentDto.getWorkExperienceCurrent())
                .workExperienceTotal(employmentDto.getWorkExperienceTotal())
                .position(employmentDto.getPosition())
                .employerInn(employmentDto.getEmployerINN())
                .build();

        statementClient.setEmployment(employment);

        logger.info("Saving client with id: {}", statementClient.getId());
        clientRepository.save(statementClient);

        statement.setStatus(ApplicationStatus.CC_APPROVED);

        StatusHistory historyElement = StatusHistory.builder()
                .time(LocalDate.now())
                .status(ApplicationStatus.CC_APPROVED)
                .changeType(ChangeType.AUTOMATIC)
                .build();

        List<StatusHistory> statusHistory = statement.getStatusHistory();
        statusHistory.add(historyElement);

        statement.setStatusHistory(statusHistory);

        logger.info("Saving statement with id {}", statement.getId());
        statementRepository.save(statement);

        ResponseEntity<CreditDto> response = restTemplate.postForEntity(
                "http://localhost:8080/calculator/calc",
                scoringDataDto,
                CreditDto.class);

        assert response.getBody() != null;

        return response.getBody();
    }
}
