package ru.shtyrev.deal_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shtyrev.deal_service.entities.Statement;

import java.util.UUID;

@Repository
public interface StatementRepository extends JpaRepository<Statement, UUID> {
}
