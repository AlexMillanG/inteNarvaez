package mx.edu.utez.inteNarvaez.models.contract;
import lombok.*;
import java.time.LocalDate;
import java.util.Date;


@Data
@Getter
@Setter
@AllArgsConstructor

public class ContractDTO {

    private Date creationDate;
    private Double amount;
    private Long address;
    private String salesPackage;



}
