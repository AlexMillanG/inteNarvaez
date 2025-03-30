package mx.edu.utez.inteNarvaez.controllers.user;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.user.UserDTO;
import mx.edu.utez.inteNarvaez.models.user.UserEntity;
import mx.edu.utez.inteNarvaez.models.user.UserRepository;
import mx.edu.utez.inteNarvaez.services.security.repository.IUserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")

public class UserControllers {
    private final IUserServiceImpl userService;
    private final UserRepository repository;
    public UserControllers(IUserServiceImpl userService, UserRepository repository) {
        this.userService = userService;
        this.repository = repository;
    }

    @GetMapping("/find-all")
    private ResponseEntity<ApiResponse> getAllUsers() {

        return new ResponseEntity<>(new ApiResponse(repository.findAll(),HttpStatus.OK,"Lista de usuarios"), HttpStatus.OK);
    }

}