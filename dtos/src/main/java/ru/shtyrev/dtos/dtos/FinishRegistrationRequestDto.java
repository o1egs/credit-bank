package ru.shtyrev.dtos.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.shtyrev.dtos.enums.Gender;
import ru.shtyrev.dtos.enums.MaritalStatus;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FinishRegistrationRequestDto {
    Gender gender;
    MaritalStatus maritalStatus;
    int dependentAmount;
    LocalDate passportIssueDate;
    String passportIssueBranch;
    EmploymentDto employment;
    String accountNumber;
}
