package mx.edu.utez.inteNarvaez.services.security.repository;

import mx.edu.utez.inteNarvaez.models.user.UserEntity;

import java.util.List;

public interface IUserServiceImpl {

    public List<UserEntity> findAllUsers();

}
