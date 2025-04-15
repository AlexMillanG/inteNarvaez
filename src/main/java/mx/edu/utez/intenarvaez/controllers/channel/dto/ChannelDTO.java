package mx.edu.utez.intenarvaez.controllers.channel.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

@Data
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
    @Pattern(regexp = "^\\d+$", message = "El campo solo puede contener números.")
    private Integer number;

    private String uuid;

    @NotNull(message = "El ID de categoría no puede ser nulo.")
    private Long categoryId;

    @NotNull(message = "La imagen no puede ser nula.")
    @Size(max = 10485760, message = "La imagen no puede exceder los 10MB.")  // Tamaño máximo de 10MB
    private MultipartFile image;



}
