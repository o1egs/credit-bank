package ru.shtyrev.dtos.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.shtyrev.dtos.enums.*;


import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmploymentDto {
    EmploymentStatus employmentStatus;
    String employerINN;
    BigDecimal salary;
    Position position;
    Integer workExperienceTotal;
    Integer workExperienceCurrent;
}
