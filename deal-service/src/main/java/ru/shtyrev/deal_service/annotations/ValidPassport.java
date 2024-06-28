//package ru.shtyrev.deal_service.annotations;
//
//import jakarta.validation.Constraint;
//import jakarta.validation.Payload;
//import ru.shtyrev.dtos.annotation.AdultValidator;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//@Target({ElementType.FIELD})
//@Retention(RetentionPolicy.RUNTIME)
//@Constraint(validatedBy = AdultValidator.class)
//public @interface ValidPassport {
//    String message() default "Невалидный паспорт";
//
//    Class<?>[] groups() default {};
//
//    Class<? extends Payload>[] payload() default {};
//}
