package mx.edu.utez.inteNarvaez.models.contract;
import lombok.*;
import jakarta.validation.constraints.*;


@Data
@Getter
@Setter
@AllArgsConstructor

public class ContractDTO {


    @NotNull(message = "El monto no puede ser nulo.")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double amount;

    @NotNull(message = "La dirección no puede ser nula.")
    @Positive(message = "El ID de dirección debe ser un ser mayor a 0")
    private Long address;

    @NotBlank(message = "El paquete de venta no puede estar vacío.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s.]+$",message = "El paquete solo puede contener letras, números, puntos y espacios.")
    private String salesPackage;

    @NotNull(message = "El ID del usuario no puede ser nulo.")
    @Positive(message = "El ID del usuario debe ser ser mayor a 0")
    private Long userId;


}
