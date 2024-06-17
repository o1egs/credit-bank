package ru.shtyrev.deal_service.entities;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import ru.shtyrev.deal_service.jsonbs.Employment;
import ru.shtyrev.deal_service.jsonbs.Passport;
import ru.shtyrev.dtos.enums.Gender;
import ru.shtyrev.dtos.enums.MaritalStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "clients")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@ToString
public class Client {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    UUID id;
//    @NotNull
//    @Size(min = 2, max = 30, message = "firstname length must be >= 2 and <= 30")
    String firstName;
//    @NotNull
//    @Size(min = 2, max = 30, message = "lastname length must be >= 2 and <= 30")
    String lastName;
    //    @Size(min = 2, max = 30, message = "middlename length must be >= 2 and <= 30")
    String middleName;
    //    @Adult
    LocalDate birthdate;
    //    @Email
//    @NotNull
    String email;
    @Enumerated(EnumType.STRING)
    Gender gender;
    @Enumerated(EnumType.STRING)
    MaritalStatus maritalStatus;
    Integer dependentAmount;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    Passport passport;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    Employment employment;
}
