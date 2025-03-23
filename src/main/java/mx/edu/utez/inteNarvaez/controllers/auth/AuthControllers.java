package mx.edu.utez.inteNarvaez.controllers.auth;

import mx.edu.utez.inteNarvaez.models.user.UserEntity;
import mx.edu.utez.inteNarvaez.services.security.repository.IAuthService;
import mx.edu.utez.inteNarvaez.models.dtos.LoginDTO;
import mx.edu.utez.inteNarvaez.models.dtos.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthControllers {


   private final IAuthService authService;

    public AuthControllers(IAuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    private ResponseEntity<ResponseDTO> regsiter(@RequestBody UserEntity user) throws Exception {
        return new ResponseEntity<>(authService.register(user), HttpStatus.OK);
        /*
           {
            "firstName": "alex",
            "lastName": "millan",
            "email": "alex1@admin.com",
            "password": "1234"
            }
         */

    }

    @PostMapping("/login")
    private  ResponseEntity<HashMap<String,String>> login(@RequestBody LoginDTO loginRequest) throws Exception {
        /*
                "email": "alex1@admin.com",
                "password": "1234"
         */
        HashMap<String,String> login = authService.login(loginRequest);
        if (login.containsKey("jwt")) {
            return new ResponseEntity<>(login,HttpStatus.OK);

        }else {
            return new ResponseEntity<>(login,HttpStatus.UNAUTHORIZED);
        }


    }
}
