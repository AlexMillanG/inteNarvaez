package mx.edu.utez.intenarvaez.config;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinimumAgeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface MinimumAge {
    String message() default "Debes tener al menos {value} años.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int value();
}
