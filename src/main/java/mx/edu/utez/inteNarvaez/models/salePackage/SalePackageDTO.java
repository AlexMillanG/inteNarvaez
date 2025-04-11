package mx.edu.utez.inteNarvaez.models.salePackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

import java.util.UUID;
@Data
@Getter
@Setter
@AllArgsConstructor
public class SalePackageDTO {

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s.]+$",message = "El nombre solo puede contener letras, números, puntos y espacios.")
    private String name;

    @NotNull(message = "El monto total no puede ser nulo.")
    @PositiveOrZero(message = "El monto total debe ser mayor o igual a 0.")
    private Double totalAmount;

    private UUID uuid;

    @NotBlank(message = "La velocidad no puede estar vacía.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$",message = "La velocidad solo puede contener letras, números y espacios.")
    private String speed;

    @NotBlank(message = "El nombre del paquete de canales no puede estar vacío.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s.]+$",message = "El nombre del paquete solo puede contener letras, números,puntos y espacios.")
    private String channel_package_name;

}
