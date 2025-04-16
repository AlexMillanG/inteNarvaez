package mx.edu.utez.intenarvaez.controllers.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mx.edu.utez.intenarvaez.config.ApiResponse;
import mx.edu.utez.intenarvaez.controllers.auth.RegisterDTO;
import mx.edu.utez.intenarvaez.services.security.services.AuthServiceImpl;
import mx.edu.utez.intenarvaez.services.security.services.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserControllers {

    private final UserServiceImpl userService;
    private final AuthServiceImpl authService;


    @GetMapping("/find-all")
    public ResponseEntity<ApiResponse> getAllUsers() {
        return new ResponseEntity<>(new ApiResponse(userService.findAll(),HttpStatus.OK,"Lista de usuarios"), HttpStatus.OK);
    }

    @PostMapping("/forward-password")
    public ResponseEntity<ApiResponse> forwardPassword(@RequestParam String password ,@RequestParam Long userId)  {
        return   authService.forwardPass(password ,userId);
    }

    @PutMapping("/UpdateAgente")
    public ResponseEntity<ApiResponse> updateAgente(@Valid @RequestBody RegisterDTO user, BindingResult result)  {
        user.setRole("USER");
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}
        return userService.updateAgente(user.toUserEntityUpdate());
    }

    @DeleteMapping("/deleteAgente/{id}")
    public ResponseEntity<ApiResponse> deleteAgente(@PathVariable Long id)  {
        return userService.deleteAgente(id);
    }


}