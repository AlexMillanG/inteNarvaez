package mx.edu.utez.intenarvaez.controllers.client;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.intenarvaez.config.anotations.ValidAgeRange;
import mx.edu.utez.intenarvaez.models.client.ClientBean;
import jakarta.validation.constraints.*;

import java.util.Date;
@Data
@Getter
@Setter
@NoArgsConstructor
public class ClientDTO {
    private Long id;
    @NotNull(message = "El nombre no puede ser nulo.")
    @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 100 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras, números y espacios.")
    private String name;

    @NotNull(message = "El apellido no puede ser nulo.")
    @Size(min = 1, max = 50, message = "El apellido debe tener entre 1 y 100 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido solo puede contener letras, números y espacios.")
    private String lastName;

    @NotNull(message = "El segundo apellido no puede ser nulo.")
    @Size(min = 1, max = 50, message = "El segundo apellido debe tener entre 1 y 100 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$", message = "El segundo apellido solo puede contener letras, números y espacios.")
    private String surname;

    @NotNull(message = "El RFC no puede ser nulo.")
    @Size(min = 12, max = 13, message = "El RFC debe tener entre 12 y 13 caracteres.")
    @Pattern(regexp = "^[A-Z&Ñ]{3,4}\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])[A-Z\\d]{2}[\\dA]$",message = "El RFC no tiene un formato válido.")
    private String rfc;

    @NotNull(message = "El correo electrónico no puede ser nulo.")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",message = "El correo electrónico no es válido.")
    private String email;

    @NotNull(message = "El teléfono no puede ser nulo.")
    @Pattern(regexp = "^\\d{10}$", message = "El teléfono debe contener 10 números.")
    private String phone;

    @NotNull(message = "La fecha de nacimiento no puede ser nula.")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada.")
    @ValidAgeRange(message = "La edad debe estar entre 18 y 100 años.")
    private Date birthdate;

    private Boolean status;
    private String uuid;


    public ClientBean toEntity() {
      return new ClientBean(name, lastName, surname, rfc, email, phone, birthdate, status, uuid);
    }
    public ClientBean toEntityUpdate() {
        return new ClientBean(id,name, lastName, surname, rfc, email, phone, birthdate, status, uuid);
    }


}
