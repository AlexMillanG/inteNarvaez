package mx.edu.utez.inteNarvaez.models.address;

import io.micrometer.common.util.StringUtils;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import org.springframework.http.HttpStatus;

public class AddressValidation {
    public static ApiResponse validate(AddressBean addressBean) {
        StringBuilder errorMessages = new StringBuilder();
        int numErrors = 0;

        if (StringUtils.isBlank(addressBean.getCity())) {
            numErrors++;
            errorMessages.append("La ciudad no puede ser nula o vacía. ");
        }

        if (StringUtils.isBlank(addressBean.getName())) {
            numErrors++;
            errorMessages.append("El nombre de la dirección no puede ser nulo o vacío. ");
        }

        if (StringUtils.isBlank(addressBean.getStreet())) {
            numErrors++;
            errorMessages.append("La calle no puede ser nula o vacía. ");
        }

        if (StringUtils.isBlank(addressBean.getState())) {
            numErrors++;
            errorMessages.append("El estado no puede ser nulo o vacío. ");
        }

        if (addressBean.getZipCode() == null) {
            numErrors++;
            errorMessages.append("El código postal no puede ser nulo. ");
        }

        if (addressBean.getNumber() == null) {
            numErrors++;
            errorMessages.append("El número no puede ser nulo. ");
        }

        if (numErrors > 0) {
            return new ApiResponse(null, HttpStatus.BAD_REQUEST, errorMessages.toString().trim(), true);
        }

        return new ApiResponse(null, HttpStatus.OK, "Datos correctos", false);
    }
}
