package ru.shtyrev.deal_service.services;

import java.util.UUID;

public interface DocumentService {
    void send(UUID statementId);

    void sign(UUID statementId);

    void code(UUID statementId);
}
