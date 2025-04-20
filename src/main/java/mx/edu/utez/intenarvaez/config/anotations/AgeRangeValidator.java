package mx.edu.utez.intenarvaez.config.anotations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class AgeRangeValidator implements ConstraintValidator<ValidAgeRange, Date> {

    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 100;

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext context) {
        if (date == null) return false;

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int age = Period.between(localDate, LocalDate.now()).getYears();
        return age >= MIN_AGE && age <= MAX_AGE;
    }
}

