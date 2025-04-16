package mx.edu.utez.intenarvaez.config.anotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileSizeValidator.class)
public @interface ValidFileSize {
    String message() default "File size exceeds limit";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    long maxSize() default 1048576; // Tamaño máximo en bytes (1MB)
}
