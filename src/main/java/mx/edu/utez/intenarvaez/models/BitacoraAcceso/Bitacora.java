package mx.edu.utez.intenarvaez.models.BitacoraAcceso;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bitacora")

public class Bitacora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idbitacora")
    private int idBitacora;

    @Column(name = "tabla", nullable = false, length = 100)
    private String tabla;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "executedSQL",  length = 2000)
    private String executedSQL;

    @Column(name = "reverseSQL",  length = 2000)
    private String reverseSQL;

}