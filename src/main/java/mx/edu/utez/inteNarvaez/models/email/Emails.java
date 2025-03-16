package mx.edu.utez.inteNarvaez.models.email;

import lombok.Getter;
import lombok.Setter;


public class Emails {

    private String destinatario;
    private String mensaje;
    private String subject;


    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
