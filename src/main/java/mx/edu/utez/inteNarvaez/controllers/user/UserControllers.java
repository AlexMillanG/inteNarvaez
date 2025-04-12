package mx.edu.utez.inteNarvaez.controllers.user;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.user.UserRepository;
import mx.edu.utez.inteNarvaez.services.security.services.AuthServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserControllers {

    private final UserRepository repository;
    private final AuthServiceImpl authService;


    @GetMapping("/find-all")
    private ResponseEntity<ApiResponse> getAllUsers() {
        return new ResponseEntity<>(new ApiResponse(repository.findAll(),HttpStatus.OK,"Lista de usuarios"), HttpStatus.OK);
    }


}