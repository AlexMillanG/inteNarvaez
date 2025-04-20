package mx.edu.utez.intenarvaez.config.anotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgeRangeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAgeRange {
    String message() default "La edad debe estar entre 18 y 110 años";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}