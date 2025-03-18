package mx.edu.utez.inteNarvaez.services.security.repository;


import mx.edu.utez.inteNarvaez.models.user.UserDTO;
import java.util.List;


public interface IUserServiceImpl {
        List<UserDTO> findAllUsers();

}
