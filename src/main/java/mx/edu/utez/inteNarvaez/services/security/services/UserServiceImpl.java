package mx.edu.utez.inteNarvaez.services.security.services;

import mx.edu.utez.inteNarvaez.models.user.UserEnitity;
import mx.edu.utez.inteNarvaez.models.user.UserRepository;
import mx.edu.utez.inteNarvaez.services.security.repository.IUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserServiceImpl {
@Autowired
    UserRepository userRepository;

    @Override
    public List<UserEnitity> findAllUsers() {
        return userRepository.findAll();
    }
}
