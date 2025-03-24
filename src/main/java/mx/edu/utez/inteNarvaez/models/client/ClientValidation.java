package mx.edu.utez.inteNarvaez.models.client;

import io.micrometer.common.util.StringUtils;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ClientValidation {

    public static ApiResponse validate(ClientBean clientBean) {
        StringBuilder errorMessages = new StringBuilder();
        int numErrors = 0;

        if (StringUtils.isBlank(clientBean.getName()) || clientBean.getName().length() < 3 || clientBean.getName().length() > 50) {
            numErrors++;
            errorMessages.append("El campo name no puede ser nulo o vacío y debe tener entre 3 y 50 caracteres. ");
        }

        if (StringUtils.isBlank(clientBean.getLastName()) || clientBean.getLastName().length() < 3 || clientBean.getLastName().length() > 50) {
            numErrors++;
            errorMessages.append("El campo lastName no puede ser nulo o vacío y debe tener entre 3 y 50 caracteres. ");
        }

        if (StringUtils.isBlank(clientBean.getSurname()) || clientBean.getSurname().length() < 3 || clientBean.getSurname().length() > 50) {
            numErrors++;
            errorMessages.append("El campo surname no puede ser nulo o vacío y debe tener entre 3 y 50 caracteres. ");
        }

        if (StringUtils.isBlank(clientBean.getPhone()) || !clientBean.getPhone().matches("^\\+?[0-9]{10}$")) {
            numErrors++;
            errorMessages.append("El campo phone debe ser válido y contener  10  dígitos. ");
        }

        if (StringUtils.isBlank(clientBean.getEmail()) || !clientBean.getEmail().matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$")) {
            numErrors++;
            errorMessages.append("El campo email no es válido. ");
        }

        if (StringUtils.isBlank(clientBean.getRfc()) || clientBean.getRfc().length() != 13) {
            numErrors++;
            errorMessages.append("El campo RFC no puede ser nulo y debe tener exactamente 13 caracteres. ");
        }

        if (numErrors > 0) {
            return new ApiResponse(null, HttpStatus.BAD_REQUEST, errorMessages.toString().trim(), true);
        }
        return new ApiResponse(clientBean, HttpStatus.OK, "Validación exitosa.", false);
    }

}
