package mx.edu.utez.inteNarvaez.services.security.repository;

import mx.edu.utez.inteNarvaez.models.user.UserEnitity;

import java.util.List;

public interface IUserServiceImpl {

    public List<UserEnitity> findAllUsers();

}
