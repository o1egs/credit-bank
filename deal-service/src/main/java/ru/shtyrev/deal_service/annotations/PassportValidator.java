//package ru.shtyrev.deal_service.annotations;
//
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//import ru.shtyrev.deal_service.jsonbs.Passport;
//
//public class PassportValidator implements ConstraintValidator<ValidPassport, Passport> {
//    @Override
//    public boolean isValid(Passport passport, ConstraintValidatorContext constraintValidatorContext) {
//        return passport.getSeries().length() == 4 && passport.getNumber().length() == 6;
//    }
//
//    @Override
//    public void initialize(ValidPassport constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation);
//    }
//}
