package ru.shtyrev.deal_service.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import ru.shtyrev.deal_service.jsonbs.StatusHistory;
import ru.shtyrev.dtos.dtos.LoanOfferDto;
import ru.shtyrev.dtos.enums.ApplicationStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "statements")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
public class Statement {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    UUID id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    Client client;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "credit_id")
    Credit credit;
    @Enumerated(EnumType.STRING)
    ApplicationStatus status;
    LocalDate creationDate;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    LoanOfferDto appliedOffer;

    LocalDate signDate;
    String sesCode;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    List<StatusHistory> statusHistory;
}
