package mx.edu.utez.intenarvaez.controllers.address.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.intenarvaez.models.address.AddressBean;
import mx.edu.utez.intenarvaez.models.client.ClientBean;
import jakarta.validation.constraints.*;

@Data
@Getter
@Setter
@NoArgsConstructor

public class AddressDTO {

    private Long id;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras, números, puntos y espacios.")
    private String name;

    @NotBlank(message = "La calle no puede estar vacía")
    @Size(max = 2556, message = "La calle no debe exceder los 2556 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s.]+$", message = "La calle solo puede contener letras, números y espacios.")
    private String street;

    @NotBlank(message = "El número no puede estar vacío")
    @Size(max = 5, message = "El número no debe exceder los 5 caracteres")
    @Pattern(regexp = "^\\d+$", message = "El campo solo puede contener números.")
    private String number;

    @NotBlank(message = "La ciudad no puede estar vacía")
    @Size(max = 50, message = "La ciudad no debe exceder los 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "La ciudad solo puede contener letras y espacios.")
    private String city;

    @NotBlank(message = "El estado no puede estar vacío")
    @Size(max = 20, message = "El estado no debe exceder los 20 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+$", message = "El estado solo puede contener letras")
    private String state;

    @NotNull(message = "El código postal es obligatorio")
    @Min(value = 10000, message = "El código postal debe tener al menos 5 dígitos")
    @Max(value = 99999, message = "El código postal no debe exceder los 5 dígitos")
    private Integer zipCode;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clientId;


    public AddressBean toEntity() {
        AddressBean addressBean = new AddressBean(name,  street,  number,  city,  state,  zipCode,clientId);
        addressBean.setId(this.id);
        addressBean.setName(this.name);
        addressBean.setStreet(this.street);
        addressBean.setNumber(this.number);
        addressBean.setCity(this.city);
        addressBean.setState(this.state);
        addressBean.setZipCode(this.zipCode);

        ClientBean clientBean = new ClientBean();
        clientBean.setId(this.clientId);
        addressBean.setClient(clientBean);
        return addressBean;
    }


}
