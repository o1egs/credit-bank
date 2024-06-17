package ru.shtyrev.deal_service.jsonbs;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Passport implements Serializable  {
    String series;
    String number;
    String issueBranch;
    String issueDate;
}
