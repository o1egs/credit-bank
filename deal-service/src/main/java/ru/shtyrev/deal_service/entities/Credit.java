package ru.shtyrev.deal_service.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import ru.shtyrev.dtos.dtos.PaymentScheduleElementDto;
import ru.shtyrev.dtos.enums.CreditStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "credits")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
public class Credit {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    UUID id;
    BigDecimal amount;
    Integer term;
    BigDecimal monthlyPayment;
    BigDecimal rate;
    BigDecimal psk;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    PaymentScheduleElementDto paymentSchedule;
    Boolean insuranceEnabled;
    Boolean salaryClient;

    CreditStatus creditStatus;
}
