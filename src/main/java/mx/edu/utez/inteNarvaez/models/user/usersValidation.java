package mx.edu.utez.inteNarvaez.models.user;

import mx.edu.utez.inteNarvaez.config.ApiResponse;
import org.springframework.http.HttpStatus;

public class usersValidation {

    public static ApiResponse validate(UserEntity user){
        StringBuilder errorMessages = new StringBuilder();
        int numErrors = 0;

        if (user.getFirstName() == null || user.getFirstName().length() < 3 || user.getFirstName().length() > 15) {
            numErrors++;
            errorMessages.append("El campo firstName no puede ser nulo y debe tener entre 3 y 15 caracteres. ");
        }

        if (user.getLastName() == null || user.getLastName().length() < 3 || user.getLastName().length() > 30) {
            numErrors++;
            errorMessages.append("El campo lastName no puede ser nulo y debe tener entre 3 y 30 caracteres. ");
        }

        if (user.getEmail() == null || !user.getEmail().matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$")) {
            numErrors++;
            errorMessages.append("El campo email no es válido. ");
        }

        if (user.getPassword() == null || !user.getPassword().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$")) {
            numErrors++;
            errorMessages.append("El campo password debe tener de 8 a 16 caracteres e incluir al menos una minúscula, una mayúscula y un número. ");
        }

        if (numErrors > 0) {
            return new ApiResponse(null, HttpStatus.BAD_REQUEST, errorMessages.toString().trim(), true);
        }
        return new ApiResponse(user, HttpStatus.OK, "Validación exitosa.", false);

    }
}