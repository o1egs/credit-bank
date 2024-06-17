package ru.shtyrev.deal_service.jsonbs;


import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.shtyrev.dtos.enums.ApplicationStatus;
import ru.shtyrev.dtos.enums.ChangeType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusHistory implements Serializable  {
    @Enumerated(EnumType.STRING)
    ApplicationStatus status;
    LocalDate time;
    @Enumerated(EnumType.STRING)
    ChangeType changeType;
}
