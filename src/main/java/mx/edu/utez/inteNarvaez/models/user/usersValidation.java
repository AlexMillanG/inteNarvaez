package mx.edu.utez.inteNarvaez.models.user;

import mx.edu.utez.inteNarvaez.models.dtos.ResponseDTO;

public class usersValidation {

    public ResponseDTO validate(UserDTO user){

        ResponseDTO responseDTO =  new ResponseDTO();
        responseDTO.setNumErrors(0);
        if (user.getFirstName() == null||
        user.getFirstName().length() <3 ||
        user.getFirstName().length()>15 ){

            responseDTO.setNumErrors(responseDTO.getNumErrors()+1);
            responseDTO.setMessage("El campo fisrtName no puede ser nulo  puede tener entre 3 y 15 caracteres");

        }
        if (user.getLastName() == null||
                user.getLastName().length() <3 ||
                user.getLastName().length()>30 ){

            responseDTO.setNumErrors(responseDTO.getNumErrors()+1);
            responseDTO.setMessage("El campo LastName no puede ser nulo , puede tener entre 3 y 30 caracteres");

        }
        if (user.getEmail() == null
                //!user.getEmail().matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$\n")

                ) {

            responseDTO.setNumErrors(responseDTO.getNumErrors()+1);
            responseDTO.setMessage("El campo email no es valido");

        }
        if (user.getPassword() == null
               // !user.getPassword().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$\n")
                ) {

            responseDTO.setNumErrors(responseDTO.getNumErrors()+1);
            responseDTO.setMessage("El campo password debe tener de 8 a 16 caracteres y debe contener una minuscula y una mayuscula");

        }
return  responseDTO;
    }
}
