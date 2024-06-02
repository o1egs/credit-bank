package ru.shtyrev.calculator_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.shtyrev.calculator_service.dtos.*;
import ru.shtyrev.calculator_service.enums.EmploymentStatus;
import ru.shtyrev.calculator_service.enums.Gender;
import ru.shtyrev.calculator_service.enums.MaritalStatus;
import ru.shtyrev.calculator_service.enums.Position;
import ru.shtyrev.calculator_service.services.CalculatorService;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class CalculatorServiceApplication implements CommandLineRunner {
	@Autowired
	CalculatorService calculatorService;

	@Override
	public void run(String... args) throws Exception {
		ScoringDataDto scoringDataDto = new ScoringDataDto();
		scoringDataDto.setAmount(new BigDecimal("1000000"));
		scoringDataDto.setTerm(36);
		scoringDataDto.setFirstName("Иван");
		scoringDataDto.setLastName("Иванов");
		scoringDataDto.setMiddleName("Иванович");
		scoringDataDto.setGender(Gender.MALE);
		scoringDataDto.setBirthdate(LocalDate.of(1985, 1, 1));
		scoringDataDto.setPassportSeries("1234");
		scoringDataDto.setPassportNumber("567890");
		scoringDataDto.setPassportIssueDate(LocalDate.of(2010, 1, 1));
		scoringDataDto.setPassportIssueBranch("Отделение УФМС");
		scoringDataDto.setMaritalStatus(MaritalStatus.MARRIED);
		scoringDataDto.setDependentAmount(2);

		EmploymentDto employmentDto = new EmploymentDto();
		employmentDto.setEmploymentStatus(EmploymentStatus.BUSINESS_OWNER);
		employmentDto.setPosition(Position.TOP_MANAGER);
		employmentDto.setSalary(new BigDecimal("100000"));
		employmentDto.setWorkExperienceTotal(200);
		employmentDto.setWorkExperienceCurrent(50);
		scoringDataDto.setEmployment(employmentDto);

		scoringDataDto.setAccountNumber("1234567890");
		scoringDataDto.setIsInsuranceEnabled(true);
		scoringDataDto.setIsSalaryClient(true);

		CreditDto creditDto = calculatorService.calc(scoringDataDto);

		System.out.println("Сумма кредита: " + creditDto.getAmount());
		System.out.println("Срок кредита: " + creditDto.getTerm() + " месяцев");
		System.out.println("Ежемесячный платеж: " + creditDto.getMonthlyPayment());
		System.out.println("Ставка: " + creditDto.getRate() + "%");
		System.out.println("Полная стоимость кредита (ПСК): " + creditDto.getPsk());
		System.out.println("График платежей:");
		for (PaymentScheduleElementDto payment : creditDto.getPaymentSchedule()) {
			System.out.println(payment);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(CalculatorServiceApplication.class, args);
	}

}
