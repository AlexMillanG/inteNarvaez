package mx.edu.utez.intenarvaez.services.security.repository;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.List;

public interface IJWTUtilityService {

    String genareteJWT(Long userID, List<String> roles)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JOSEException;

    JWTClaimsSet parseJWT(String jwt)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, ParseException, JOSEException;

}
