package mx.edu.utez.inteNarvaez.controllers.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utez.inteNarvaez.config.MinimumAge;
import mx.edu.utez.inteNarvaez.models.role.RoleBean;
import mx.edu.utez.inteNarvaez.models.user.UserEntity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
public class RegisterDTO {

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

    @NotNull(message = "La fecha de nacimiento no puede estar vacía.")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada.")
    @MinimumAge(value = 18, message = "Debes tener al menos 18 años.")
    private Date birthdate;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._#-])[A-Za-z\\d@$!%*?&._#-]{8,}$",message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.")
    private String password;
    private String role;

    public UserEntity toUserEntity() {
        UserEntity user = new UserEntity();
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setSurname(this.surname);
        user.setEmail(this.email);
        user.setRfc(this.rfc);
        user.setPhone(this.phone);
        user.setBirthdate(this.birthdate);
        user.setPassword(this.password);
        user.setStatus(true);
        user.setTemporalPassword(false);
        Set<RoleBean> roles = new HashSet<>();
        roles.add(toRoleBean(role));
        user.setRoleBeans(roles);
        user.setLastLogin(null);
        return user;
    }
    public UserEntity toUserEntityUpdate() {
        UserEntity user = new UserEntity();
        user.setId(this.id);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setSurname(this.surname);
        user.setEmail(this.email);
        user.setRfc(this.rfc);
        user.setPhone(this.phone);
        user.setBirthdate(this.birthdate);
        user.setPassword(this.password);
        user.setStatus(true);
        user.setTemporalPassword(false);
        Set<RoleBean> roles = new HashSet<>();
        roles.add(toRoleBean(role));
        user.setRoleBeans(roles);
        user.setLastLogin(null);
        return user;
    }

    public RoleBean toRoleBean(String role) {
        RoleBean roleBean = new RoleBean();
        roleBean.setName(role);
        return roleBean;
    }




}
