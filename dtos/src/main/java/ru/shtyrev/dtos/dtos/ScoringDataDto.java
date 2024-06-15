package ru.shtyrev.dtos.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.shtyrev.dtos.enums.*;


import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoringDataDto {
    BigDecimal amount;
    Integer term;
    String firstName;
    String lastName;
    String middleName;
    Gender gender;
    LocalDate birthdate;
    String passportSeries;
    String passportNumber;
    LocalDate passportIssueDate;
    String passportIssueBranch;
    MaritalStatus maritalStatus;
    Integer dependentAmount;
    EmploymentDto employment;
    String accountNumber;
    Boolean isInsuranceEnabled;
    Boolean isSalaryClient;
}
