package mx.edu.utez.inteNarvaez.controllers.email;

import jakarta.mail.MessagingException;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.email.Emails;
import mx.edu.utez.inteNarvaez.services.email.emailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/email")
public class emailController {
    private final emailService service;

    public emailController(emailService service) {
        this.service = service;
    }


    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendEmail(@RequestBody Emails emails) throws MessagingException {
        return service.sendMail(emails);
    }

}
