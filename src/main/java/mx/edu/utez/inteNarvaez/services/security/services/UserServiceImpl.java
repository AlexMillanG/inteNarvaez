package mx.edu.utez.inteNarvaez.services.security.services;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.models.user.UserDTO;
import mx.edu.utez.inteNarvaez.models.user.UserEntity;
import mx.edu.utez.inteNarvaez.models.user.UserRepository;
import mx.edu.utez.inteNarvaez.services.security.repository.IUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserServiceImpl {

    private final  UserRepository userRepository;

    public List<UserDTO> findAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(userEntity -> new UserDTO(userEntity))
                .collect(Collectors.toList());
    }

}