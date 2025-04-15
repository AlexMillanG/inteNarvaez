package mx.edu.utez.inteNarvaez.controllers.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.controllers.auth.RegisterDTO;
import mx.edu.utez.inteNarvaez.services.security.services.AuthServiceImpl;
import mx.edu.utez.inteNarvaez.services.security.services.UserServiceImpl;
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
    private ResponseEntity<ApiResponse> getAllUsers() {
        return new ResponseEntity<>(new ApiResponse(userService.findAll(),HttpStatus.OK,"Lista de usuarios"), HttpStatus.OK);
    }

    @PostMapping("/forward-password")
    private ResponseEntity<ApiResponse> forwardPassword(@RequestParam String password ,@RequestParam Long userId) throws Exception {
        return   authService.forwardPass(password ,userId);
    }

    @PutMapping("/UpdateAgente")
    private ResponseEntity<ApiResponse> updateAgente(@Valid @RequestBody RegisterDTO user, BindingResult result) throws Exception {
        user.setRole("USER");
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}
        return userService.updateAgente(user.toUserEntityUpdate());
    }

    @DeleteMapping("/deleteAgente/{id}")
    private ResponseEntity<ApiResponse> deleteAgente(@PathVariable Long id) throws Exception {
        return userService.deleteAgente(id);
    }


}