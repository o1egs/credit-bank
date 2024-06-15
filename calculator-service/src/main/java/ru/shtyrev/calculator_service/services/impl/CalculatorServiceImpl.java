package ru.shtyrev.calculator_service.services.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import ru.shtyrev.calculator_service.services.CalculatorService;
import ru.shtyrev.dtos.dtos.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "credit-properties")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculatorServiceImpl implements CalculatorService {

    static final Logger logger = LoggerFactory.getLogger(CalculatorServiceImpl.class);

    @Setter
    Integer loanRate;
    final Validator validator;

    @Override
    public List<LoanOfferDto> offers(@Valid LoanStatementRequestDto loanStatementRequestDto) {
        logger.info("Received loan statement request: {}", loanStatementRequestDto);

        var constraintViolations = validator.validate(loanStatementRequestDto);
        if (!constraintViolations.isEmpty()) {
            for (ConstraintViolation<LoanStatementRequestDto> violation : constraintViolations) {
                logger.error("Validation error: {}", violation.getMessage());
            }
            throw new RuntimeException("Validation failed");
        }

        LoanOfferDto loanOfferDto1 = loanOfferDto(loanStatementRequestDto, true, true);
        LoanOfferDto loanOfferDto2 = loanOfferDto(loanStatementRequestDto, true, false);
        LoanOfferDto loanOfferDto3 = loanOfferDto(loanStatementRequestDto, false, false);
        LoanOfferDto loanOfferDto4 = loanOfferDto(loanStatementRequestDto, false, true);

        List<LoanOfferDto> offers = Stream.of(loanOfferDto1, loanOfferDto2, loanOfferDto3, loanOfferDto4)
                .sorted(Comparator.comparing(LoanOfferDto::getRate).reversed())
                .toList();

        logger.info("Generated loan offers: {}", offers);
        return offers;
    }

    private LoanOfferDto loanOfferDto(LoanStatementRequestDto loanStatementRequestDto,
                                      Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        logger.debug("Calculating loan offer for: {}, isInsuranceEnabled: {}, isSalaryClient: {}",
                loanStatementRequestDto, isInsuranceEnabled, isSalaryClient);

        BigDecimal requestAmount = loanStatementRequestDto.getAmount();
        BigDecimal rate = new BigDecimal(loanRate);
        BigDecimal totalAmount;

        if (isInsuranceEnabled) {
            BigDecimal insuranceAmount = requestAmount.divide(BigDecimal.TEN, 4, RoundingMode.HALF_UP);
            totalAmount = requestAmount.add(insuranceAmount).setScale(4, RoundingMode.HALF_UP);
            rate = rate.subtract(new BigDecimal(3)).setScale(4, RoundingMode.HALF_UP);
        } else {
            totalAmount = requestAmount.setScale(4, RoundingMode.HALF_UP);
            rate = rate.add(new BigDecimal(1)).setScale(4, RoundingMode.HALF_UP);
        }

        if (isSalaryClient) {
            rate = rate.subtract(new BigDecimal(1)).setScale(4, RoundingMode.HALF_UP);
        }

        BigDecimal monthlyRate = rate.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP)
                .divide(new BigDecimal(12), 4, RoundingMode.HALF_UP);
        int term = loanStatementRequestDto.getTerm();

        BigDecimal numerator = monthlyRate.multiply(totalAmount)
                .multiply((monthlyRate.add(BigDecimal.ONE)).pow(term));
        BigDecimal denominator = (monthlyRate.add(BigDecimal.ONE))
                .pow(term).subtract(BigDecimal.ONE);
        BigDecimal monthlyPayment = numerator.divide(denominator, 4, RoundingMode.HALF_UP);
        BigDecimal totalPaymentAmount = monthlyPayment.multiply(new BigDecimal(term)).setScale(4, RoundingMode.HALF_UP);

        LoanOfferDto loanOfferDto = LoanOfferDto.builder()
                .statementId(UUID.randomUUID())
                .requestAmount(requestAmount)
                .totalAmount(totalPaymentAmount)
                .term(term)
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();

        logger.debug("Generated loan offer: {}", loanOfferDto);
        return loanOfferDto;
    }

    @Override
    public CreditDto calc(ScoringDataDto scoringDataDto) {
        logger.info("Received scoring data: {}", scoringDataDto);

        BigDecimal rate = new BigDecimal(loanRate);
        BigDecimal amount = scoringDataDto.getAmount();
        int term = scoringDataDto.getTerm();
        BigDecimal monthlySalary = scoringDataDto.getEmployment().getSalary();

        int age = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();
        if (age < 20 || age > 65) {
            logger.warn("Applicant's age {} is out of acceptable range", age);
            return null;
        }

        if (amount.compareTo(monthlySalary.multiply(new BigDecimal(25))) > 0) {
            logger.warn("Requested amount {} exceeds acceptable limit based on salary {}", amount, monthlySalary);
            return null;
        }

        switch (scoringDataDto.getEmployment().getEmploymentStatus()) {
            case UNEMPLOYED:
                logger.warn("Applicant is unemployed");
                return null;
            case SELF_EMPLOYED:
                rate = rate.add(new BigDecimal("1.0")).setScale(4, RoundingMode.HALF_UP);
                break;
            case BUSINESS_OWNER:
                rate = rate.add(new BigDecimal("2.0")).setScale(4, RoundingMode.HALF_UP);
                break;
            default:
                break;
        }

        switch (scoringDataDto.getEmployment().getPosition()) {
            case MIDDLE_MANAGER:
                rate = rate.subtract(new BigDecimal("2.0")).setScale(4, RoundingMode.HALF_UP);
                break;
            case TOP_MANAGER:
                rate = rate.subtract(new BigDecimal("3.0")).setScale(4, RoundingMode.HALF_UP);
                break;
            default:
                break;
        }

        switch (scoringDataDto.getMaritalStatus()) {
            case MARRIED:
                rate = rate.subtract(new BigDecimal("3.0")).setScale(4, RoundingMode.HALF_UP);
                break;
            case DIVORCED:
                rate = rate.add(new BigDecimal("1.0")).setScale(4, RoundingMode.HALF_UP);
                break;
            default:
                break;
        }

        switch (scoringDataDto.getGender()) {
            case FEMALE:
                if (age >= 32 && age <= 60) {
                    rate = rate.subtract(new BigDecimal("3.0")).setScale(4, RoundingMode.HALF_UP);
                }
                break;
            case MALE:
                if (age >= 30 && age <= 55) {
                    rate = rate.subtract(new BigDecimal("3.0")).setScale(4, RoundingMode.HALF_UP);
                }
                break;
            case NON_BINARY:
                rate = rate.add(new BigDecimal("7.0")).setScale(4, RoundingMode.HALF_UP);
                break;
            default:
                break;
        }

        int totalMonthsWorked = scoringDataDto.getEmployment().getWorkExperienceTotal();
        int currentMonthsWorked = scoringDataDto.getEmployment().getWorkExperienceCurrent();

        if (totalMonthsWorked < 18 || currentMonthsWorked < 3) {
            logger.warn("Insufficient work experience: total {}, current {}", totalMonthsWorked, currentMonthsWorked);
            return null;
        }

        if (scoringDataDto.getIsSalaryClient()) {
            rate = rate.subtract(new BigDecimal("1.0")).setScale(4, RoundingMode.HALF_UP);
        }
        if (scoringDataDto.getIsInsuranceEnabled()) {
            rate = rate.subtract(new BigDecimal("0.5")).setScale(4, RoundingMode.HALF_UP);
        }

        BigDecimal monthlyRate = rate.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)
                .divide(new BigDecimal("12"), 4, RoundingMode.HALF_UP);
        BigDecimal annuityCoefficient = monthlyRate.add(BigDecimal.ONE).pow(term).multiply(monthlyRate)
                .divide((monthlyRate.add(BigDecimal.ONE).pow(term)).subtract(BigDecimal.ONE), 4, RoundingMode.HALF_UP);
        BigDecimal monthlyPayment = amount.multiply(annuityCoefficient).setScale(4, RoundingMode.HALF_UP);

        BigDecimal psk = rate.multiply(new BigDecimal(term)).divide(new BigDecimal("12"), 4, RoundingMode.HALF_UP);

        List<PaymentScheduleElementDto> paymentSchedule = new ArrayList<>();
        BigDecimal remainingDebt = amount;
        for (int i = 1; i <= term; i++) {
            BigDecimal interestPayment = remainingDebt.multiply(monthlyRate).setScale(4, RoundingMode.HALF_UP);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment).setScale(4, RoundingMode.HALF_UP);
            remainingDebt = remainingDebt.subtract(debtPayment).setScale(4, RoundingMode.HALF_UP);

            PaymentScheduleElementDto paymentElement = new PaymentScheduleElementDto();
            paymentElement.setNumber(i);
            paymentElement.setDate(LocalDate.now().plusMonths(i));
            paymentElement.setTotalPayment(monthlyPayment);
            paymentElement.setInterestPayment(interestPayment);
            paymentElement.setDebtPayment(debtPayment);
            paymentElement.setRemainingDebt(remainingDebt);
            paymentSchedule.add(paymentElement);
        }

        CreditDto creditDto = new CreditDto();
        creditDto.setAmount(amount.setScale(4, RoundingMode.HALF_UP));
        creditDto.setTerm(term);
        creditDto.setMonthlyPayment(monthlyPayment);
        creditDto.setRate(rate);
        creditDto.setPsk(psk);
        creditDto.setIsInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled());
        creditDto.setIsSalaryClient(scoringDataDto.getIsSalaryClient());
        creditDto.setPaymentSchedule(paymentSchedule);

        logger.info("Generated credit details: {}", creditDto);
        return creditDto;
    }
}
