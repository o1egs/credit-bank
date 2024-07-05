package ru.shtyrev.dossier_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import ru.shtyrev.dossier_service.services.MailService;
import ru.shtyrev.dtos.dtos.EmailMessage;
import ru.shtyrev.dtos.enums.Theme;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.file.Path;

@SpringBootApplication
public class DossierServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DossierServiceApplication.class, args);
    }
}
