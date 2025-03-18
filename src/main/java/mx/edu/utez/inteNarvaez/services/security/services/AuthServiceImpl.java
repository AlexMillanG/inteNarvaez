package mx.edu.utez.inteNarvaez.services.security.services;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.models.role.RoleDTO;
import mx.edu.utez.inteNarvaez.models.user.UserDTO;
import mx.edu.utez.inteNarvaez.models.user.UserEntity;
import mx.edu.utez.inteNarvaez.models.user.UserRepository;
import mx.edu.utez.inteNarvaez.models.dtos.LoginDTO;
import mx.edu.utez.inteNarvaez.models.dtos.ResponseDTO;
import mx.edu.utez.inteNarvaez.models.user.usersValidation;
import mx.edu.utez.inteNarvaez.services.security.repository.IAuthService;
import mx.edu.utez.inteNarvaez.services.security.repository.IJWTUtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;

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

    @Override
    public ResponseDTO register(UserEntity user) throws Exception {
        return null;
    }

    public ResponseDTO register(UserDTO userDTO) throws Exception {
        try {
            ResponseDTO responseDTO = usersValidations.validate(userDTO);

            if (responseDTO.getNumErrors() > 0) {
                return responseDTO;
            }

            Optional<UserEntity> getUsers = userRepository.findByEmail(userDTO.getEmail());

            if (getUsers.isPresent()) {
                responseDTO.setNumErrors(1);
                responseDTO.setMessage("User already exists");
                return responseDTO;
            }

            // Convertir UserDTO a UserEntity
            UserEntity userEntity = new UserEntity();
            userEntity.setFirstName(userDTO.getFirstName());
            userEntity.setLastName(userDTO.getLastName());
            userEntity.setEmail(userDTO.getEmail());

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            userEntity.setPassword(encoder.encode(userDTO.getPassword()));

            userRepository.save(userEntity);
            responseDTO.setMessage("User created successfully");

            return responseDTO;
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    private boolean verifyPassword(String enteredPassword, String storedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(enteredPassword, storedPassword);
    }


}
