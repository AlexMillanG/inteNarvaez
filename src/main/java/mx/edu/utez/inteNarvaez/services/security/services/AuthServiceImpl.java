package mx.edu.utez.inteNarvaez.services.security.services;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.controllers.auth.RegisterDTO;
import mx.edu.utez.inteNarvaez.models.email.Emails;
import mx.edu.utez.inteNarvaez.models.role.RoleBean;
import mx.edu.utez.inteNarvaez.models.role.RoleRepository;
import mx.edu.utez.inteNarvaez.models.user.UserDTO;
import mx.edu.utez.inteNarvaez.models.user.UserEntity;
import mx.edu.utez.inteNarvaez.models.user.UserRepository;
import mx.edu.utez.inteNarvaez.models.dtos.LoginDTO;
import mx.edu.utez.inteNarvaez.services.email.EmailService;
import mx.edu.utez.inteNarvaez.services.security.repository.IAuthService;
import mx.edu.utez.inteNarvaez.services.security.repository.IJWTUtilityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private  final UserServiceImpl userServiceImpl;
    private final RoleRepository roleRepository;
    private final IJWTUtilityService jwtUtilityService;

    private final EmailService emailService;

    private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);

    @Override
    public HashMap<String, String> login(LoginDTO loginDTO) throws Exception {
        try {
            HashMap<String, String> jwt = new HashMap<>();
            Optional<UserEntity> user = userRepository.findByEmail(loginDTO.getEmail());

            if (user.isEmpty()) {
                jwt.put("error", "User not registered!");
                return jwt;
            }

            if (verifyPassword(loginDTO.getPassword(), user.get().getPassword())) {
                UserDTO userDTO = new UserDTO(user.get());

                jwt.put("jwt", jwtUtilityService.genareteJWT(
                        userDTO.getId(),
                        userDTO.getRoles().stream().collect(Collectors.toList())
                ));

                if (user.get().isTemporalPassword()) {
                    jwt.put("temporal", "true");
                }

            } else {
                jwt.put("error", "Authentication failed!");
            }

            return jwt;
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<ApiResponse> register(UserDTO.RegisterDTO register) {

        try {
            UserEntity userEntity = register.getUser();

            Optional<UserEntity> existingUser = userRepository.findByEmail(userEntity.getEmail());
            if (existingUser.isPresent()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El uasurio ya esta registrado", true), HttpStatus.BAD_REQUEST);
            }
            Optional<RoleBean> role = roleRepository.findByName(register.getName());
            if (role.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "El rol no existe", true), HttpStatus.NOT_FOUND);
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            userEntity.setPassword(encoder.encode(userEntity.getPassword()));

            UserEntity createdUser = userRepository.save(userEntity);
            userRepository.insertRoles(createdUser.getId(), role.get().getId());


            return new ResponseEntity<>(new ApiResponse(createdUser, HttpStatus.CREATED, "Usuario creado correctamente"), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error(e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, e.getMessage(), true), HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            logger.error("Error al registrar el usuario: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al acceder a la base de datos", true), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Error al registrar el usuario: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error desconocido: " + e.getMessage(), true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> forwardPassword(String email) throws Exception {
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
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, e.getMessage(), true), HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al acceder a la base de datos", true), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error desconocido: " + e.getMessage(), true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> forwardPass(String password ,Long id) throws Exception {
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