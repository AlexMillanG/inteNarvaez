package mx.edu.utez.intenarvaez.controllers.auth;

import com.nimbusds.jose.JOSEException;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mx.edu.utez.intenarvaez.config.ApiResponse;
import mx.edu.utez.intenarvaez.models.BitacoraAcceso.BitacoraAccesoEntity;
import mx.edu.utez.intenarvaez.models.BitacoraAcceso.BitacoraAccesoRepository;
import mx.edu.utez.intenarvaez.models.role.RoleRepository;
import mx.edu.utez.intenarvaez.models.user.UserEntity;
import mx.edu.utez.intenarvaez.models.user.UserRepository;
import mx.edu.utez.intenarvaez.services.BitacoraAcceso.BitacoraAccesoService;
import mx.edu.utez.intenarvaez.services.contract.ContractService;
import mx.edu.utez.intenarvaez.services.security.repository.IAuthService;
import mx.edu.utez.intenarvaez.models.Auth.LoginDTO;
import mx.edu.utez.intenarvaez.services.security.services.AuthServiceImpl;
import mx.edu.utez.intenarvaez.services.security.services.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    private static final Logger logger = LogManager.getLogger(AuthControllers.class);




    @PostMapping("/registerAgente")
    public ResponseEntity<ApiResponse> registeUser(@Valid @Validated(OnCreate.class)@RequestBody RegisterDTO user, BindingResult result)  {
        user.setRole("USER");
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}

        return userService.registerAgente(user.toUserEntity());
    }
  


    @GetMapping ("/roles")
    public ResponseEntity<ApiResponse> roles() {
        return new ResponseEntity<>(new ApiResponse(repository.findAll(), HttpStatus.OK,"ROLES"),HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody LoginDTO loginRequest,
            HttpServletRequest request) {

        try {
            Map<String, String> login = authService.login(loginRequest);

            Optional<UserEntity> foundUser = userRepository.findByEmail(loginRequest.getEmail());
            boolean exito = login.containsKey("jwt");

            if (foundUser.isPresent()) {
                bitacoraAccesoService.guardarBitacoraAcceso(request, foundUser.get(), exito,
                        exito ? "Login exitoso" : "Contraseña incorrecta");

                if (exito) {
                    UserEntity user = foundUser.get();
                    user.setLastLogin(new Date());
                    userRepository.save(user);
                }

            } else {
                BitacoraAccesoEntity bitacora = new BitacoraAccesoEntity();
                bitacora.setUsuario(loginRequest.getEmail());
                bitacora.setIpOrigen(request.getRemoteAddr());
                bitacora.setUserAgent(request.getHeader("User-Agent"));
                bitacora.setExito(false);
                bitacora.setMensaje("Usuario no registrado");
                bitacoraRepository.save(bitacora);
            }

            return new ResponseEntity<>(login, exito ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);

        } catch (AuthServiceImpl.AuthException e) {
            BitacoraAccesoEntity bitacora = new BitacoraAccesoEntity();
            bitacora.setUsuario(loginRequest.getEmail());
            bitacora.setIpOrigen(request.getRemoteAddr());
            bitacora.setUserAgent(request.getHeader("User-Agent"));
            bitacora.setExito(false);
            bitacora.setMensaje(e.getMessage());
            bitacoraRepository.save(bitacora);

            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (AuthException | IOException | NoSuchAlgorithmException | InvalidKeySpecException | JOSEException e) {
           logger.error(e.getMessage());
            return new ResponseEntity<>( new HashMap<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/forward-password")
    public  ResponseEntity<ApiResponse> forwardPassword(@RequestParam String email) throws AuthException {
       return authService.forwardPassword(String.valueOf(email));
    }


}