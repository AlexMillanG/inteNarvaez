package mx.edu.utez.inteNarvaez.services.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.email.Emails;
import mx.edu.utez.inteNarvaez.models.email.emailsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public  class emailService implements emailsRepository {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    protected emailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public ResponseEntity<ApiResponse> sendMail(Emails emails) throws MessagingException {

        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");

            helper.setTo(emails.getDestinatario());
            helper.setSubject(emails.getSubject());
            Context context = new Context();
            context.setVariable("message", emails.getMensaje());
            String contentHTML = templateEngine.process("email",context);

            helper.setText(contentHTML,true);
            javaMailSender.send(message);

         return new ResponseEntity<>(new ApiResponse(null, HttpStatus.OK ,"El correo fue enviado exitosamente"),HttpStatus.OK);

        }catch (Exception ex){
            throw  new RuntimeException("Error ala enviar al correo"+ex.getMessage(),ex);

        }

    }

}


