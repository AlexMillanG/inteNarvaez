package mx.edu.utez.inteNarvaez.controllers.user;

import mx.edu.utez.inteNarvaez.models.user.UserDTO;
import mx.edu.utez.inteNarvaez.models.user.UserEntity;
import mx.edu.utez.inteNarvaez.services.security.repository.IUserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserControllers {
    private final IUserServiceImpl userService;
    public UserControllers(IUserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/find-all")
    private ResponseEntity<List<UserDTO>> getAllUsers() {

        List<UserDTO> usersDTO = userService.findAllUsers();
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }

}
