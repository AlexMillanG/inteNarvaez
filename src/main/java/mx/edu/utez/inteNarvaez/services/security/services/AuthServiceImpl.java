package mx.edu.utez.inteNarvaez.services.security.services;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.role.RoleBean;
import mx.edu.utez.inteNarvaez.models.role.RoleRepository;
import mx.edu.utez.inteNarvaez.models.user.UserDTO;
import mx.edu.utez.inteNarvaez.models.user.UserEntity;
import mx.edu.utez.inteNarvaez.models.user.UserRepository;
import mx.edu.utez.inteNarvaez.models.dtos.LoginDTO;
import mx.edu.utez.inteNarvaez.models.dtos.ResponseDTO;
import mx.edu.utez.inteNarvaez.models.user.usersValidation;
import mx.edu.utez.inteNarvaez.services.security.repository.IAuthService;
import mx.edu.utez.inteNarvaez.services.security.repository.IJWTUtilityService;
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
    private final RoleRepository roleRepository;


    private final IJWTUtilityService jwtUtilityService;


    private final usersValidation usersValidations;

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

                jwt.put("jwt", jwtUtilityService.genareteJWT(userDTO.getId(), userDTO.getRoles().stream().collect(Collectors.toList())));
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

        //  ApiResponse responseDTO = usersValidation.validate(register.getUser());
        //   if (responseDTO.isError()) {return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);}

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
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, e.getMessage(), true), HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al acceder a la base de datos", true), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error desconocido: " + e.getMessage(), true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean verifyPassword(String enteredPassword, String storedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(enteredPassword, storedPassword);
    }


}
