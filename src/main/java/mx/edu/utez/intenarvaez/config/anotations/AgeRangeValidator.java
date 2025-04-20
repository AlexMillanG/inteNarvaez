package mx.edu.utez.intenarvaez.config.anotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeRangeValidator implements ConstraintValidator<ValidAgeRange, LocalDate> {

    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 100;

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) return false;

        int age = Period.between(date, LocalDate.now()).getYears();
        return age >= MIN_AGE && age <= MAX_AGE;
    }
}
