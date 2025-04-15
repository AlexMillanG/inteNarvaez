package mx.edu.utez.intenarvaez.services.security.services;

import lombok.AllArgsConstructor;

import mx.edu.utez.intenarvaez.config.ApiResponse;
import mx.edu.utez.intenarvaez.models.Auth.LoginDTO;
import mx.edu.utez.intenarvaez.models.email.Emails;
import mx.edu.utez.intenarvaez.models.role.RoleBean;
import mx.edu.utez.intenarvaez.models.user.UserEntity;
import mx.edu.utez.intenarvaez.models.user.UserRepository;
import mx.edu.utez.intenarvaez.services.email.EmailService;
import mx.edu.utez.intenarvaez.services.security.repository.IAuthService;
import mx.edu.utez.intenarvaez.services.security.repository.IJWTUtilityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private  final UserServiceImpl userServiceImpl;
    private final IJWTUtilityService jwtUtilityService;

    private final EmailService emailService;

    private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);

    @Override
    public HashMap<String, String> login(LoginDTO loginDTO)  {
        try {
            HashMap<String, String> jwt = new HashMap<>();
            Optional<UserEntity> user = userRepository.findByEmail(loginDTO.getEmail());

            if (user.isEmpty()) {
                jwt.put("error", "Usuario no registrado");
                return jwt;
            }

            if (!user.get().getStatus()) {
                jwt.put("error", "El usuario ya no está activo");
                return jwt;
            }

            if (verifyPassword(loginDTO.getPassword(), user.get().getPassword())) {
                jwt.put("jwt", jwtUtilityService.genareteJWT(
                        user.get().getId(),
                        user.get().getRoleBeans().stream().map(RoleBean::getName).toList())
                );

                    if (user.get().isTemporalPassword()) {
                        jwt.put("temporal", "true");
                    }

                } else {
                    jwt.put("error", "Authentication failed!");

            }

            return jwt;
        } catch (Exception e) {
            logger.error(e);

        }
        return null;
    }




    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> forwardPassword(String email) {
        try {
            Optional<UserEntity> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "El usuario no existe", true), HttpStatus.NOT_FOUND);
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            String newPassword = userServiceImpl.generatePassword();
            user.get().setPassword(encoder.encode(newPassword));
            user.get().setTemporalPassword(true);

            userRepository.saveAndFlush(user.get());

           Emails emails = new Emails(user.get().getEmail(), newPassword, "Restauracion de contraseña");
            emailService.sendEmail(emails, 1);

            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.OK, "Correo enviado correctamente"), HttpStatus.OK);
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


}