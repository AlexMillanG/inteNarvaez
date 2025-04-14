package mx.edu.utez.intenarvaez.services.security.repository;

import com.nimbusds.jose.JOSEException;
import jakarta.security.auth.message.AuthException;
import mx.edu.utez.intenarvaez.config.ApiResponse;
import mx.edu.utez.intenarvaez.models.Auth.LoginDTO;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public interface IAuthService {

  public Map<String, String> login(LoginDTO loginDTO) throws AuthException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, JOSEException;
     public ResponseEntity<ApiResponse> forwardPassword(String email) throws AuthException;

    }
