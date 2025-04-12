package mx.edu.utez.inteNarvaez.controllers.auth;

import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.role.RoleRepository;
import mx.edu.utez.inteNarvaez.models.user.UserDTO;
import mx.edu.utez.inteNarvaez.models.user.UserEntity;
import mx.edu.utez.inteNarvaez.models.user.UserRepository;
import mx.edu.utez.inteNarvaez.services.security.repository.IAuthService;
import mx.edu.utez.inteNarvaez.models.dtos.LoginDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/auth")

public class AuthControllers {
    private final IAuthService authService;
    private final RoleRepository repository;
    private final UserRepository userRepository;

    public AuthControllers(IAuthService authService, RoleRepository repository, UserRepository userRepository) {
        this.authService = authService;
        this.repository = repository;
        this.userRepository = userRepository;
    }
    @PostMapping("/registerUser")
    private ResponseEntity<ApiResponse> registeUser(@RequestBody UserDTO.RegisterDTO user) throws Exception {
        user.setName("USER");
        return authService.register(user);
    }

    @PostMapping("/registerAgente")
    private ResponseEntity<ApiResponse> registeUser(@RequestBody RegisterDTO user) throws Exception {
        user.setRole("USER");
        return authService.registerAgente(user);
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
            Optional<UserEntity> foundUser = userRepository.findByEmail(loginRequest.getEmail());
            UserEntity user = foundUser.get();
            user.setLastLogin(new Date());
            userRepository.save(user);  

            return new ResponseEntity<>(login,HttpStatus.OK);

        }else {
            return new ResponseEntity<>(login,HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/forward-password")
    private  ResponseEntity<ApiResponse> forwardPassword(@RequestParam String email) throws Exception {
       return authService.forwardPassword(String.valueOf(email));
    }


}