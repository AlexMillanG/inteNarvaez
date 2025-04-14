package mx.edu.utez.intenarvaez.services.security.services;

import com.nimbusds.jose.JOSEException;
import lombok.AllArgsConstructor;
import mx.edu.utez.intenarvaez.config.ApiResponse;
import mx.edu.utez.intenarvaez.models.email.Emails;
import mx.edu.utez.intenarvaez.models.role.RoleBean;
import mx.edu.utez.intenarvaez.models.user.UserEntity;
import mx.edu.utez.intenarvaez.models.user.UserRepository;
import mx.edu.utez.intenarvaez.models.Auth.LoginDTO;
import mx.edu.utez.intenarvaez.services.email.EmailService;
import mx.edu.utez.intenarvaez.services.security.repository.IAuthService;
import mx.edu.utez.intenarvaez.services.security.repository.IJWTUtilityService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private  final UserServiceImpl userServiceImpl;
    private final IJWTUtilityService jwtUtilityService;
    private final EmailService emailService;

    @Override
    public Map<String, String> login(LoginDTO loginDTO) throws AuthException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, JOSEException {
        Map<String, String> jwt = new HashMap<>();
        Optional<UserEntity> user = userRepository.findByEmail(loginDTO.getEmail());

        if (user.isEmpty()) {
            throw new AuthException("User not registered!");
        }

        if (verifyPassword(loginDTO.getPassword(), user.get().getPassword())) {
            jwt.put("jwt", jwtUtilityService.genareteJWT(
                    user.get().getId(),
                    user.get().getRoleBeans().stream().map(RoleBean::getName).collect(Collectors.toList())
            ));

            if (user.get().isTemporalPassword()) {
                jwt.put("temporal", "true");
            }

            return jwt;
        } else {
            throw new AuthException("Authentication failed!");
        }
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> forwardPassword(String email) throws AuthException {
        try {
            Optional<UserEntity> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "El usuario no existe", true), HttpStatus.NOT_FOUND);
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            String newPassword = UserServiceImpl.generatePassword();
            user.get().setPassword(encoder.encode(newPassword));
            user.get().setTemporalPassword(true);

            userRepository.saveAndFlush(user.get());

            Emails emails = new Emails(user.get().getEmail(), newPassword, "Restauracion de contraseña");
            emailService.sendEmail(emails, 1);

            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.OK, "Correo enviado correctamente"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, e.getMessage(), true), HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al acceder a la base de datos", true), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error desconocido: " + e.getMessage(), true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> forwardPass(String password ,Long id)  {
        try {
            Optional<UserEntity> user = userRepository.findById(id);
            if (user.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "El usuario no existe", true), HttpStatus.NOT_FOUND);
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            user.get().setPassword(encoder.encode(password));
            user.get().setTemporalPassword(false);

            userRepository.saveAndFlush(user.get());
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.OK, "Actualizacion exitosa"), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error : " + e.getMessage(), true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     private boolean verifyPassword(String enteredPassword, String storedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(enteredPassword, storedPassword);
    }
    public static class AuthException extends RuntimeException {
        public AuthException(String message) {
            super(message);
        }
    }


}