package mx.edu.utez.intenarvaez.config.anotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<ValidFileSize, MultipartFile> {

    private long maxSize;

    @Override
    public void initialize(ValidFileSize constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return file == null || file.getSize() <= maxSize;
    }
}

