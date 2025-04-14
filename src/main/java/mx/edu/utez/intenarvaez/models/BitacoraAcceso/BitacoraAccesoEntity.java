package mx.edu.utez.intenarvaez.models.BitacoraAcceso;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utez.intenarvaez.models.user.UserEntity;

import java.util.Date;

@Entity
@Table(name = "bitacora_accesos")
@Getter
@Setter
public class BitacoraAccesoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private UserEntity user;

    private String usuario;

    @Column(name = "fecha_acceso", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date fechaAcceso = new Date();

    @Column(name = "ip_origen")
    private String ipOrigen;

    @Column(name = "user_agent")
    private String userAgent;

    private Boolean exito;

    private String mensaje;


}
