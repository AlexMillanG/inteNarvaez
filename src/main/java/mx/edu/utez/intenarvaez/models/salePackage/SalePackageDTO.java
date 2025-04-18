package mx.edu.utez.intenarvaez.models.salePackage;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalePackageDTO {

    private  Long id;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s.]+$",message = "El nombre solo puede contener letras, números, puntos y espacios.")
    private String name;

    @NotNull(message = "El monto total no puede ser nulo.")
    @PositiveOrZero(message = "El monto total debe ser mayor o igual a 0.")
    private Double totalAmount;

    private String uuid;

    @NotBlank(message = "La velocidad no puede estar vacía.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$",message = "La velocidad solo puede contener letras, números y espacios.")
    private String speed;

    @NotBlank(message = "El nombre del paquete de canales no puede estar vacío.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s.]+$",message = "El nombre del paquete solo puede contener letras, números,puntos y espacios.")
    private String channel_package_name;


}
