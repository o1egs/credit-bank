package ru.shtyrev.deal_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.shtyrev.deal_service.entities.Client;
import ru.shtyrev.deal_service.jsonbs.Employment;
import ru.shtyrev.deal_service.jsonbs.Passport;
import ru.shtyrev.deal_service.repositories.ClientRepository;
import ru.shtyrev.dtos.enums.EmploymentStatus;
import ru.shtyrev.dtos.enums.Gender;
import ru.shtyrev.dtos.enums.MaritalStatus;
import ru.shtyrev.dtos.enums.Position;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@SpringBootApplication
public class DealServiceApplication implements CommandLineRunner {
	@Autowired
	ClientRepository clientRepository;

	public static void main(String[] args) {
		SpringApplication.run(DealServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		Client client = Client.builder()
//				.firstName("Oleg")
//				.lastName("Shtyrev")
//				.middleName("Igorevich")
//				.birthdate(LocalDate.now().minusYears(20))
//				.email("o@mail.ru")
//				.gender(Gender.MALE)
//				.maritalStatus(MaritalStatus.SINGLE)
//				.dependentAmount(100)
//				.passport(Passport.builder()
//						.number("1")
//						.series("1")
//						.issueBranch("1")
//						.issueDate("1")
//						.build())
//				.employment(Employment.builder()
//						.employerInn("cascas")
//						.salary(BigDecimal.TEN)
//						.position(Position.MIDDLE_MANAGER)
//						.status(EmploymentStatus.EMPLOYED)
//						.workExperienceCurrent(10)
//						.workExperienceTotal(10)
//						.build())
//				.build();
//		Client saved = clientRepository.save(client);
//		System.out.println(saved);
//		Client client = clientRepository.findById(UUID.fromString("c9a465b9-f0e7-4972-adf3-b8b819484d03")).orElseThrow();
//		System.out.println(client);
//		System.out.println(client.getPassport());
//		System.out.println(client.getEmployment());
	}
}
