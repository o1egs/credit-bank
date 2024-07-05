package ru.shtyrev.dossier_service.services;

import ru.shtyrev.dtos.dtos.EmailMessage;

import javax.mail.MessagingException;

public interface MailService {
    void sendMail(EmailMessage emailMessage) throws MessagingException;
}
