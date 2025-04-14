package mx.edu.utez.inteNarvaez.controllers.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.BitacoraAcceso.BitacoraAccesoEntity;
import mx.edu.utez.inteNarvaez.models.BitacoraAcceso.BitacoraAccesoRepository;
import mx.edu.utez.inteNarvaez.models.role.RoleRepository;
import mx.edu.utez.inteNarvaez.models.user.UserDTO;
import mx.edu.utez.inteNarvaez.models.user.UserEntity;
import mx.edu.utez.inteNarvaez.models.user.UserRepository;
import mx.edu.utez.inteNarvaez.services.BitacoraAcceso.BitacoraAccesoService;
import mx.edu.utez.inteNarvaez.services.security.repository.IAuthService;
import mx.edu.utez.inteNarvaez.models.Auth.LoginDTO;
import mx.edu.utez.inteNarvaez.services.security.services.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthControllers {
    private final IAuthService authService;
    private final RoleRepository repository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final BitacoraAccesoService bitacoraAccesoService;
    private final BitacoraAccesoRepository bitacoraRepository;


    @PostMapping("/registerUser")
    private ResponseEntity<ApiResponse> registeUser(@RequestBody UserDTO.RegisterDTO user) throws Exception {
        user.setName("USER");
        return authService.register(user);
    }

    @PostMapping("/registerAgente")
    private ResponseEntity<ApiResponse> registeUser(@Valid @Validated(OnCreate.class)@RequestBody RegisterDTO user, BindingResult result) throws Exception {
        user.setRole("USER");
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}

        return userService.registerAgente(user.toUserEntity());
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
    private ResponseEntity<HashMap<String, String>> login(
            @RequestBody LoginDTO loginRequest,
            HttpServletRequest request) throws Exception {

        HashMap<String, String> login = authService.login(loginRequest);

        Optional<UserEntity> foundUser = userRepository.findByEmail(loginRequest.getEmail());
        boolean exito = login.containsKey("jwt");

        // Guarda bitácora (con o sin usuario válido)
        if (foundUser.isPresent()) {
            bitacoraAccesoService.guardarBitacoraAcceso(request, foundUser.get(), exito,
                    exito ? "Login exitoso" : "Contraseña incorrecta");
        } else {
            // En caso de usuario no registrado
            BitacoraAccesoEntity bitacora = new BitacoraAccesoEntity();
            bitacora.setUsuario(loginRequest.getEmail());
            bitacora.setIpOrigen(request.getRemoteAddr());
            bitacora.setUserAgent(request.getHeader("User-Agent"));
            bitacora.setExito(false);
            bitacora.setMensaje("Usuario no registrado");
            bitacoraRepository.save(bitacora);
        }

        // Si login fue exitoso, guarda lastLogin
        if (exito && foundUser.isPresent()) {
            UserEntity user = foundUser.get();
            user.setLastLogin(new Date());
            userRepository.save(user);
            return new ResponseEntity<>(login, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(login, HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/forward-password")
    private  ResponseEntity<ApiResponse> forwardPassword(@RequestParam String email) throws Exception {
       return authService.forwardPassword(String.valueOf(email));
    }


}