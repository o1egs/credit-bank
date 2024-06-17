package ru.shtyrev.deal_service.jsonbs;


import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.shtyrev.dtos.enums.EmploymentStatus;
import ru.shtyrev.dtos.enums.Position;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Employment implements Serializable {
    @Enumerated(EnumType.STRING)
    EmploymentStatus status;
    String employerInn;
    BigDecimal salary;
    @Enumerated(EnumType.STRING)
    Position position;
    Integer workExperienceTotal;
    Integer workExperienceCurrent;
}