package mx.edu.utez.inteNarvaez.services.security.repository;

import mx.edu.utez.inteNarvaez.models.user.UserEnitity;
import mx.edu.utez.inteNarvaez.models.dtos.LoginDTO;
import mx.edu.utez.inteNarvaez.models.dtos.ResponseDTO;

import java.util.HashMap;

public interface IAuthService {

    public HashMap<String,String> login(LoginDTO loginDTO) throws Exception;
    public ResponseDTO register(UserEnitity user) throws Exception;

    }
