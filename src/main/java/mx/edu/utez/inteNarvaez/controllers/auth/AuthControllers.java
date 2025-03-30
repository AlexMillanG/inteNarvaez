package mx.edu.utez.inteNarvaez.controllers.auth;

import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.role.RoleRepository;
import mx.edu.utez.inteNarvaez.models.user.UserDTO;
import mx.edu.utez.inteNarvaez.services.security.repository.IAuthService;
import mx.edu.utez.inteNarvaez.models.dtos.LoginDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")

public class AuthControllers {
    private final IAuthService authService;
    private final RoleRepository repository;

    public AuthControllers(IAuthService authService, RoleRepository repository) {
        this.authService = authService;
        this.repository = repository;
    }
    @PostMapping("/register")
    private ResponseEntity<ApiResponse> regsiter(@RequestBody UserDTO.RegisterDTO user) throws Exception {
        return authService.register(user);
    }

    @GetMapping ("/roles")
    private ResponseEntity<ApiResponse> roles() throws Exception {
        return new ResponseEntity<>(new ApiResponse(repository.findAll(), HttpStatus.OK,"ROLES"),HttpStatus.OK);
    }

    @PostMapping("/login")
    private  ResponseEntity<HashMap<String,String>> login(@RequestBody LoginDTO loginRequest) throws Exception {
        HashMap<String,String> login = authService.login(loginRequest);
        if (login.containsKey("jwt")) {
            return new ResponseEntity<>(login,HttpStatus.OK);

        }else {
            return new ResponseEntity<>(login,HttpStatus.UNAUTHORIZED);
        }


    }
}