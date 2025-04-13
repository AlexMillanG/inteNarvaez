package mx.edu.utez.inteNarvaez.services.security.services;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.role.RoleBean;
import mx.edu.utez.inteNarvaez.models.role.RoleRepository;
import mx.edu.utez.inteNarvaez.models.user.UserEntity;
import mx.edu.utez.inteNarvaez.models.user.UserRepository;
import mx.edu.utez.inteNarvaez.services.security.repository.IUserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserServiceImpl {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_=+<>?";
    private static final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL;
    private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);

    private static final int PASSWORD_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();

    public static String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));

        for (int i = 3; i < PASSWORD_LENGTH; i++) {
            password.append(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }
        List<Character> passwordChars = new ArrayList<>();
        for (char c : password.toString().toCharArray()) {
            passwordChars.add(c);
        }
        Collections.shuffle(passwordChars);
        StringBuilder finalPassword = new StringBuilder();
        for (char c : passwordChars) {
            finalPassword.append(c);
        }
        return finalPassword.toString();
    }


    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll() {
        try {
            if (userRepository.findAll().isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND ,"No hay usuarios registrados") , HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ApiResponse(userRepository.findAll(), HttpStatus.OK,"Lista de usuarios"), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.INTERNAL_SERVER_ERROR,"Error al obtener la lista de usuarios"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> registerAgente(UserEntity userEntity) {

        try {
            Optional<UserEntity> existingUser = userRepository.findByEmail(userEntity.getEmail());
            if (existingUser.isPresent()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El uasurio ya esta registrado", true), HttpStatus.BAD_REQUEST);
            }
            Optional<RoleBean> role = roleRepository.findByName(userEntity.getRoleBeans().iterator().next().getName());
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

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> updateAgente(UserEntity userEntity) {
        try {
            if (userEntity.getId() == null || userEntity.getId() <= 0 ) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id no puede ser nullo", true), HttpStatus.BAD_REQUEST);
            }
            Optional<UserEntity> existingUser = userRepository.findById(userEntity.getId());
            if (existingUser.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "El usuario no existe", true), HttpStatus.NOT_FOUND);
            }
            Optional<RoleBean> role = roleRepository.findByName(userEntity.getRoleBeans().iterator().next().getName());
            if (role.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "El rol no existe", true), HttpStatus.NOT_FOUND);
            }

            userEntity.setStatus(existingUser.get().getStatus());
            userEntity.setLastLogin(existingUser.get().getLastLogin());
            userEntity.setPassword(existingUser.get().getPassword());
            userEntity.setRoleBeans(existingUser.get().getRoleBeans());

            UserEntity createdUser = userRepository.saveAndFlush(userEntity);

            return new ResponseEntity<>(new ApiResponse(createdUser, HttpStatus.OK, "Usuario actualizo correctamente"), HttpStatus.OK);
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

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> deleteAgente(Long userId) {
        try {
            if (userId == null || userId <= 0) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id no puede ser nulo", true), HttpStatus.BAD_REQUEST);
            }

            Optional<UserEntity> existingUser = userRepository.findById(userId);
            if (existingUser.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "El usuario no existe", true), HttpStatus.NOT_FOUND);
            }
            existingUser.get().setStatus(false);
            userRepository.saveAndFlush(existingUser.get());
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.OK, "Usuario desabilitado correctamente"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.error(e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, e.getMessage(), true), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR,"Algo salio mal"+ e.getMessage(), true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}