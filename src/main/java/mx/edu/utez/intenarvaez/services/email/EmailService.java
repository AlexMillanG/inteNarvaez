package mx.edu.utez.intenarvaez.services.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import mx.edu.utez.intenarvaez.config.ApiResponse;
import mx.edu.utez.intenarvaez.models.channelPackage.ChannelPackageBean;
import mx.edu.utez.intenarvaez.models.email.Emails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.List;


@Service
@AllArgsConstructor
public  class EmailService  {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    private static final Logger logger = LogManager.getLogger(EmailService.class);

    public void sendEmail(Emails email , int plantilla) throws MessagingException {
        try {

            Context context = new Context();

            context.setVariable("message", email.getMensaje());
            context.setVariable("name", email.getDestinatario());

            String[] plantillaAlerta = new String[]{"verificacion","email","alerta"};

            String htmlContent = templateEngine.process(plantillaAlerta[plantilla], context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email.getDestinatario());

            helper.setText(htmlContent, true);
            helper.setFrom("ultranet.sa.d.cv@gmail.com");

            javaMailSender.send(message);
            logger.info("Correo HTML enviado con exito a {}", email.getDestinatario());

            new ResponseEntity<>(new ApiResponse(email, HttpStatus.OK, "El email se envio correctamente"), HttpStatus.OK);

        } catch (Exception e) {

            logger.error("Error al enviar el coreo {}", e.getMessage());
            new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "No se envio el email"), HttpStatus.OK);

        }

    }

@Transactional(rollbackFor = Exception.class)
    public void UpdatePackageEmail(List<String> destinatarios , ChannelPackageBean packageBean) throws MessagingException {
        try {
            Context context = new Context();
               context.setVariable("channels", packageBean.getChannels());

            String htmlContent = templateEngine.process("updatePackage", context);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(destinatarios.toArray(new String[0]));
            helper.setText(htmlContent, true);
            helper.setFrom("ultranet.sa.d.cv@gmail.com");
            helper.setSubject("Actualización de Paquete de Canales");
            javaMailSender.send(message);
            logger.info("Correo HTML enviado con exito");
            new ResponseEntity<>(new ApiResponse(null, HttpStatus.OK, "El email se envió correctamente"), HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al enviar el correo {} " , e.getMessage());

            new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "No se envió el email"), HttpStatus.OK);
        }
    }


}


