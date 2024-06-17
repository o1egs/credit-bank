package ru.shtyrev.deal_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shtyrev.deal_service.entities.Credit;

import java.util.UUID;

@Repository
public interface CreditRepository extends JpaRepository<Credit, UUID> {
}
