package mx.edu.utez.inteNarvaez.services.security.repository;

import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.controllers.auth.RegisterDTO;
import mx.edu.utez.inteNarvaez.models.user.UserDTO;
import mx.edu.utez.inteNarvaez.models.dtos.LoginDTO;
import mx.edu.utez.inteNarvaez.models.user.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public interface IAuthService {

    public HashMap<String,String> login(LoginDTO loginDTO) throws Exception;
    public ResponseEntity<ApiResponse> register(UserDTO.RegisterDTO user) throws Exception;
    public ResponseEntity<ApiResponse> forwardPassword(String email) throws Exception;

    }
