package ru.shtyrev.dtos.dtos;


import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.shtyrev.dtos.annotation.Adult;


import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoanStatementRequestDto {
    @NotNull
    @DecimalMin(value = "30000", message = "amount must be >= 30000")
    BigDecimal amount;
    @NotNull
    @Min(value = 6, message = "term must be >= 6")
    Integer term;
    @NotNull
    @Size(min = 2, max = 30, message = "firstname length must be >= 2 and <= 30")
    String firstName;
    @NotNull
    @Size(min = 2, max = 30, message = "lastname length must be >= 2 and <= 30")
    String lastName;
    @Size(min = 2, max = 30, message = "middlename length must be >= 2 and <= 30")
    String middleName;
    @Email
    @NotNull
    String email;
    @Adult
    LocalDate birthdate;
    @NotNull
    @Size(min = 4, max = 4, message = "passportSeries length must be 4")
    @Pattern(regexp = "\\d{4}", message = "passportSeries must contain only digits")
    String passportSeries;
    @NotNull
    @Size(min = 6, max = 6, message = "passportSeries length must be 4")
    @Pattern(regexp = "\\d{6}", message = "passportNumber must contain only digits")
    String passportNumber;
}
