package mx.edu.utez.intenarvaez.models.email;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class Emails {

    private String destinatario;
    private String mensaje;
    private String subject;

}
