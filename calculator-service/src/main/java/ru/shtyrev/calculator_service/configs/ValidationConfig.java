package ru.shtyrev.calculator_service.configs;

import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

@Configuration
public class ValidationConfig {
    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        validatorFactory.close();
        return validator;
    }
}
