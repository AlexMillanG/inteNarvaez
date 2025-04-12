package mx.edu.utez.inteNarvaez.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.validation.constraints.*;
import mx.edu.utez.inteNarvaez.config.MinimumAge;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras y espacios.")
    private String firstName;

    @NotBlank(message = "El apellido paterno no puede estar vacío.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido paterno solo puede contener letras y espacios.")
    private String lastName;

    @NotBlank(message = "El apellido materno no puede estar vacío.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido materno solo puede contener letras, y espacios.")
    private String surname;

    @NotBlank(message = "El correo electrónico no puede estar vacío.")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",message = "El correo electrónico no es válido.")
    private String email;

    @NotBlank(message = "El RFC no puede estar vacío.")
    @Pattern(regexp = "^[A-Z&Ñ]{3,4}[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])[A-Z0-9]{2}[0-9A]$",message = "El RFC no tiene un formato válido.")
    private String rfc;

    @NotBlank(message = "El número de teléfono no puede estar vacío.")
    @Pattern(regexp = "^[0-9]{10}$", message = "El número de teléfono debe contener 10 dígitos.")
    private String phone;

    @NotBlank(message = "La fecha de nacimiento no puede estar vacía.")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada.")
    @MinimumAge(value = 18, message = "Debes tener al menos 18 años.")
    private Date birthdate;


    @JsonIgnore
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._-])[A-Za-z\\d@$!%*?&._-]{8,}$",message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.")
    private String password;

    @JsonIgnore
    private Set<String> roles;

    public UserDTO(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.email = userEntity.getEmail();
        this.roles = userEntity.getRoleBeans().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());
    }
    @Getter
    @Setter
    @Data
    public static  class RegisterDTO {
        private UserEntity user;
        private String name;

        public RegisterDTO(UserEntity user, String name) {
            this.user = user;
            this.name = name;
        }


    }

}