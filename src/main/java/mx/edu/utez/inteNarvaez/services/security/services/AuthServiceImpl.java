package mx.edu.utez.inteNarvaez.services.security.services;

import mx.edu.utez.inteNarvaez.models.user.UserEnitity;
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
import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IJWTUtilityService jwtUtilityService;

    @Autowired
    private  usersValidation usersValidations;
    @Override
    public HashMap<String,String> login(LoginDTO loginDTO) throws Exception {
        try {
            HashMap<String,String> jwt = new HashMap<>();
            Optional<UserEnitity> user =userRepository.findByEmail(loginDTO.getEmail());

            if (user.isEmpty()){
                jwt.put("error","user not register!");
                return jwt;
            }
            if (veriifyPassword(loginDTO.getPassword(),user.get().getPassword() )){

                jwt.put("jwt",jwtUtilityService.genareteJWT(user.get().getId()));
            } else {

                jwt.put("error","Authentication failed!");

            }

            return jwt;

        }catch (Exception e){

            throw  new Exception(e.toString());

        }
    }


    public ResponseDTO register(UserEnitity user) throws Exception {
        try {

            ResponseDTO responseDTO = usersValidations.validate(user);

            if (responseDTO.getNumErrors()>0){
                return  responseDTO;
            }

         Optional<UserEnitity> getUsers =userRepository.findByEmail(user.getEmail());



                if (getUsers.isPresent()){
                    responseDTO.setNumErrors(1);
                    responseDTO.setMessage("User already exist");
                    return responseDTO;
                }


            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            user.setPassword(encoder.encode(user.getPassword()));

            userRepository.save(user);
            responseDTO.setMessage("User created succesfully");

            return  responseDTO;
        }catch (Exception e){
            throw new Exception(e.toString());
        }
    }

    private boolean veriifyPassword(String enteredPassword,String storePassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return  encoder.matches(enteredPassword,storePassword);

    }


}
