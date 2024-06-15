package ru.shtyrev.deal_service.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.shtyrev.dtos.annotation.Adult;
import ru.shtyrev.dtos.enums.Gender;
import ru.shtyrev.dtos.enums.MaritalStatus;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @NotNull
    @Size(min = 2, max = 30, message = "firstname length must be >= 2 and <= 30")
    String firstName;
    @NotNull
    @Size(min = 2, max = 30, message = "lastname length must be >= 2 and <= 30")
    String lastName;
    @Size(min = 2, max = 30, message = "middlename length must be >= 2 and <= 30")
    String middleName;
    @Adult
    LocalDate birthdate;
    @Email
    @NotNull
    String email;
    @Enumerated(EnumType.STRING)
    Gender gender;
    @Enumerated(EnumType.STRING)
    MaritalStatus maritalStatus;
    Integer dependentAmount;
    
}
