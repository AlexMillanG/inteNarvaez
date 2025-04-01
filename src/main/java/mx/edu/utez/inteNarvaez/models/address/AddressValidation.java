package mx.edu.utez.inteNarvaez.models.address;

import io.micrometer.common.util.StringUtils;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import org.springframework.http.HttpStatus;

public class AddressValidation {

    public static final int NAME_MAX_LENGTH = 100;
    public static final int STREET_MAX_LENGTH = 256;
    public static final int CITY_MAX_LENGTH = 50;
    public static final int STATE_MAX_LENGTH = 20;

    public static ApiResponse validate(AddressBean addressBean) {
        StringBuilder errorMessages = new StringBuilder();
        int numErrors = 0;

        // Validar el nombre
        if (StringUtils.isBlank(addressBean.getName())) {
            numErrors++;
            errorMessages.append("El nombre de la dirección no puede ser nulo o vacío. ");
        } else if (addressBean.getName().length() > NAME_MAX_LENGTH) {
            numErrors++;
            errorMessages.append("El nombre de la dirección no puede tener más de ").append(NAME_MAX_LENGTH).append(" caracteres. ");
        }

        // Validar la calle
        if (StringUtils.isBlank(addressBean.getStreet())) {
            numErrors++;
            errorMessages.append("La calle no puede ser nula o vacía. ");
        } else if (addressBean.getStreet().length() > STREET_MAX_LENGTH) {
            numErrors++;
            errorMessages.append("La calle no puede tener más de ").append(STREET_MAX_LENGTH).append(" caracteres. ");
        }

        // Validar la ciudad
        if (StringUtils.isBlank(addressBean.getCity())) {
            numErrors++;
            errorMessages.append("La ciudad no puede ser nula o vacía. ");
        } else if (addressBean.getCity().length() > CITY_MAX_LENGTH) {
            numErrors++;
            errorMessages.append("La ciudad no puede tener más de ").append(CITY_MAX_LENGTH).append(" caracteres. ");
        }

        // Validar el estado
        if (StringUtils.isBlank(addressBean.getState())) {
            numErrors++;
            errorMessages.append("El estado no puede ser nulo o vacío. ");
        } else if (addressBean.getState().length() > STATE_MAX_LENGTH) {
            numErrors++;
            errorMessages.append("El estado no puede tener más de ").append(STATE_MAX_LENGTH).append(" caracteres. ");
        }

        // Validar el código postal
        if (addressBean.getZipCode() == null) {
            numErrors++;
            errorMessages.append("El código postal no puede ser nulo. ");
        } else if (addressBean.getZipCode() < 10000 || addressBean.getZipCode() > 99999) {
            numErrors++;
            errorMessages.append("El código postal no es valido, debe contener 5 dígitos. ");
        }

        if (StringUtils.isBlank(addressBean.getNumber())) {
            numErrors++;
            errorMessages.append("El número no puede ser nulo o vacío. ");
        } else if (!addressBean.getNumber().equalsIgnoreCase("SN") && !addressBean.getNumber().matches("\\d{1,5}")) {
            numErrors++;
            errorMessages.append("El número debe ser 'SN' o un valor numérico de hasta 5 dígitos. ");
        }

        if (numErrors > 0) {
            return new ApiResponse(null, HttpStatus.BAD_REQUEST, errorMessages.toString().trim(), true);
        }

        return new ApiResponse(null, HttpStatus.OK, "Datos correctos", false);
    }
}