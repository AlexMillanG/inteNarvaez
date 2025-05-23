package mx.edu.utez.intenarvaez.controllers.channel.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utez.intenarvaez.config.anotations.ValidFileSize;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
public class ChannelDTO {

    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras, números y espacios.")
    private String name;

    @NotBlank(message = "La descripción no puede estar vacía.")
    @Size(max = 255, message = "La descripción no debe exceder los 255 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s.]+$", message = "La descripcion solo puede contener letras, números, puntos y espacios.")
    private String description;

    @NotNull(message = "El número no puede ser nulo.")
    @Digits(integer = 3, fraction = 0, message = "El número debe ser un entero de hasta 3 dígitos.")
    @Min(value = 1, message = "El número debe ser mayor o igual a 1")
    private Integer number;

    private String uuid;

    @NotNull(message = "El ID de categoría no puede ser nulo.")
    private Long categoryId;

    @ValidFileSize(maxSize = 1048576, message = "El tamaño del archivo debe ser menor a 1MB.")
    private MultipartFile image;

    private Boolean keepImage;

    public ChannelDTO() {// Noncompliant - method is empty
    }
}
