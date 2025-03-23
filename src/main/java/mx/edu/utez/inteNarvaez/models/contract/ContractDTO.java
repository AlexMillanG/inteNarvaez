package mx.edu.utez.inteNarvaez.models.contract;
import lombok.*;
import java.time.LocalDate;


@Data
@Getter
@Setter
@AllArgsConstructor

public class ContractDTO {

    private LocalDate creationDate;
    private Double amount;

    private String address;
    private String salesPackage;



}
