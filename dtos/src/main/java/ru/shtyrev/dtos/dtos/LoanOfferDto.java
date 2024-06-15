package ru.shtyrev.dtos.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoanOfferDto {
    UUID statementId;
    BigDecimal requestAmount;
    BigDecimal totalAmount;
    Integer term;
    BigDecimal monthlyPayment;
    BigDecimal rate;
    Boolean isInsuranceEnabled;
    Boolean isSalaryClient;
}
