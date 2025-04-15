package mx.edu.utez.intenarvaez.config.anotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class MinimumAgeValidator implements ConstraintValidator<MinimumAge, Date> {

    private int age;

    @Override
    public void initialize(MinimumAge constraintAnnotation) {
        this.age = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Date birthdate, ConstraintValidatorContext context) {
        if (birthdate == null) return false;

        LocalDate birth = new java.sql.Date(birthdate.getTime()).toLocalDate();
        return Period.between(birth, LocalDate.now()).getYears() >= age;
    }
}