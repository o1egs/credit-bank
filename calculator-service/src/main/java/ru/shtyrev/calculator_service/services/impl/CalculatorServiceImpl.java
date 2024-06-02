package ru.shtyrev.calculator_service.services.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import ru.shtyrev.calculator_service.dtos.*;
import ru.shtyrev.calculator_service.services.CalculatorService;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "credit-properties")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculatorServiceImpl implements CalculatorService {
    @Setter
    Integer loanRate;
    final Validator validator;

    @Override
    public List<LoanOfferDto> offers(LoanStatementRequestDto loanStatementRequestDto) {
        var constraintViolations = validator.validate(loanStatementRequestDto);
        if (!constraintViolations.isEmpty()) {
            throw new RuntimeException();
        }

        LoanOfferDto loanOfferDto1 = loanOfferDto(loanStatementRequestDto, true, true);
        LoanOfferDto loanOfferDto2 = loanOfferDto(loanStatementRequestDto, true, false);
        LoanOfferDto loanOfferDto3 = loanOfferDto(loanStatementRequestDto, false, false);
        LoanOfferDto loanOfferDto4 = loanOfferDto(loanStatementRequestDto, false, true);

        return Stream.of(loanOfferDto1, loanOfferDto2, loanOfferDto3, loanOfferDto4)
                .sorted(Comparator.comparing(LoanOfferDto::getRate).reversed())
                .toList();
    }

    private LoanOfferDto loanOfferDto(LoanStatementRequestDto loanStatementRequestDto,
                                      Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        BigDecimal requestAmount = loanStatementRequestDto.getAmount();
        BigDecimal rate = new BigDecimal(loanRate);
        BigDecimal totalAmount;

        if (isInsuranceEnabled) {
            BigDecimal insuranceAmount = requestAmount.divide(BigDecimal.TEN);
            totalAmount = requestAmount.add(insuranceAmount);
            rate = rate.subtract(new BigDecimal(3));
        } else {
            totalAmount = requestAmount;
            rate = rate.add(new BigDecimal(1));
        }

        if (isSalaryClient) {
            rate = rate.subtract(new BigDecimal(1));
        }

        BigDecimal monthlyRate = rate.divide(new BigDecimal(100)).divide(new BigDecimal(12), MathContext.DECIMAL128);

        int term = loanStatementRequestDto.getTerm();

        BigDecimal numerator = monthlyRate.multiply(totalAmount)
                .multiply((monthlyRate.add(BigDecimal.ONE)).pow(term));
        BigDecimal denominator = (monthlyRate.add(BigDecimal.ONE))
                .pow(term).subtract(BigDecimal.ONE);
        BigDecimal monthlyPayment = numerator.divide(denominator, MathContext.DECIMAL128);

        BigDecimal totalPaymentAmount = monthlyPayment.multiply(new BigDecimal(term));

        return LoanOfferDto.builder()
                .statementId(UUID.randomUUID())
                .requestAmount(requestAmount)
                .totalAmount(totalPaymentAmount)
                .term(term)
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();
    }





    @Override
    public CreditDto calc(ScoringDataDto scoringDataDto) {
        BigDecimal baseRate = new BigDecimal(loanRate);
        BigDecimal rate = baseRate;
        BigDecimal amount = scoringDataDto.getAmount();
        int term = scoringDataDto.getTerm();
        BigDecimal monthlySalary = scoringDataDto.getEmployment().getSalary();

        int age = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();
        if (age < 20 || age > 65) {
            return null;
        }

        if (amount.compareTo(monthlySalary.multiply(new BigDecimal(25))) > 0) {
            return null;
        }

        switch (scoringDataDto.getEmployment().getEmploymentStatus()) {
            case UNEMPLOYED:
                return null; // Отказ по статусу безработного
            case SELF_EMPLOYED:
                rate = rate.add(new BigDecimal("1.0"));
                break;
            case BUSINESS_OWNER:
                rate = rate.add(new BigDecimal("2.0"));
                break;
            default:
                break;
        }

        switch (scoringDataDto.getEmployment().getPosition()) {
            case MIDDLE_MANAGER:
                rate = rate.subtract(new BigDecimal("2.0"));
                break;
            case TOP_MANAGER:
                rate = rate.subtract(new BigDecimal("3.0"));
                break;
            default:
                break;
        }

        switch (scoringDataDto.getMaritalStatus()) {
            case MARRIED:
                rate = rate.subtract(new BigDecimal("3.0"));
                break;
            case DIVORCED:
                rate = rate.add(new BigDecimal("1.0"));
                break;
            default:
                break;
        }

        switch (scoringDataDto.getGender()) {
            case FEMALE:
                if (age >= 32 && age <= 60) {
                    rate = rate.subtract(new BigDecimal("3.0"));
                }
                break;
            case MALE:
                if (age >= 30 && age <= 55) {
                    rate = rate.subtract(new BigDecimal("3.0"));
                }
                break;
            case NON_BINARY:
                rate = rate.add(new BigDecimal("7.0"));
                break;
            default:
                break;
        }

        int totalMonthsWorked = scoringDataDto.getEmployment().getWorkExperienceTotal();
        int currentMonthsWorked = scoringDataDto.getEmployment().getWorkExperienceCurrent();

        if (totalMonthsWorked < 18 || currentMonthsWorked < 3) {
            return null;
        }

        if (scoringDataDto.getIsSalaryClient()) {
            rate = rate.subtract(new BigDecimal("1.0"));
        }
        if (scoringDataDto.getIsInsuranceEnabled()) {
            rate = rate.subtract(new BigDecimal("0.5"));
        }

        BigDecimal monthlyRate = rate.divide(new BigDecimal("100"), MathContext.DECIMAL64).divide(new BigDecimal("12"), MathContext.DECIMAL64);
        BigDecimal annuityCoefficient = monthlyRate.add(BigDecimal.ONE).pow(term).multiply(monthlyRate)
                .divide((monthlyRate.add(BigDecimal.ONE).pow(term)).subtract(BigDecimal.ONE), MathContext.DECIMAL64);
        BigDecimal monthlyPayment = amount.multiply(annuityCoefficient);

        BigDecimal psk = rate.multiply(new BigDecimal(term)).divide(new BigDecimal("12"), MathContext.DECIMAL64);

        List<PaymentScheduleElementDto> paymentSchedule = new ArrayList<>();
        BigDecimal remainingDebt = amount;
        for (int i = 1; i <= term; i++) {
            BigDecimal interestPayment = remainingDebt.multiply(monthlyRate);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment);

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
        creditDto.setAmount(amount);
        creditDto.setTerm(term);
        creditDto.setMonthlyPayment(monthlyPayment);
        creditDto.setRate(rate);
        creditDto.setPsk(psk);
        creditDto.setIsInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled());
        creditDto.setIsSalaryClient(scoringDataDto.getIsSalaryClient());
        creditDto.setPaymentSchedule(paymentSchedule);

        return creditDto;
    }

}
