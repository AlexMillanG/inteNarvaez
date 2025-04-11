package mx.edu.utez.inteNarvaez.controllers.email;

import jakarta.mail.MessagingException;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.email.Emails;
import mx.edu.utez.inteNarvaez.services.email.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class emailController {
    private final EmailService service;

    public emailController(EmailService service) {
        this.service = service;
    }


    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendEmail(@RequestBody Emails emails) throws MessagingException {
        return service.sendEmail(emails,1);
    }

}
