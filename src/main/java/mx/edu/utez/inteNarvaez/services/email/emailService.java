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
            System.out.println("Entramos");
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");

            helper.setTo(emails.getDestinatario());
            helper.setSubject(emails.getSubject());
            helper.setFrom("utezdoces@gmail.com");  // Asegúrate de colocar aquí el correo del remitente
            Context context = new Context();
            context.setVariable("message", emails.getMensaje());
            String contentHTML = templateEngine.process("email",context);

            helper.setText(contentHTML,true);

            System.out.println("antes de enviar");
            javaMailSender.send(message);

         return new ResponseEntity<>(new ApiResponse(null, HttpStatus.OK ,"El correo fue enviado exitosamente"),HttpStatus.OK);

        }catch (Exception ex){
            System.out.println(ex);
            throw  new RuntimeException("Error ala enviar al correo"+ex.getMessage(),ex);

        }

    }

    public ResponseEntity<ApiResponse> sendEmail(Emails email) throws MessagingException {
        try {
            // Crear contexto de Thymeleaf
            Context context = new Context();

            context.setVariable("message", email.getMensaje());
            context.setVariable("name", email.getDestinatario());

            String[] plantillaAlerta = new String[]{"verificacion"};

            // Procesar la plantilla y generar el contenido HTML
            String htmlContent = templateEngine.process(plantillaAlerta[0], context);

            // Crear el mensaje MIME
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email.getDestinatario());

            helper.setText(htmlContent, true);
            helper.setFrom("utezdoces@gmail.com");

            // Enviar el correo
            javaMailSender.send(message);
            System.out.println("Correo HTML enviado con éxito a " + email.getDestinatario());

            return new ResponseEntity<>(new ApiResponse(email, HttpStatus.OK, "El email se envio correctamente"), HttpStatus.OK);

        } catch (Exception e) {

            System.out.println(e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "No se envio el email"), HttpStatus.OK);

        }

    }

}


