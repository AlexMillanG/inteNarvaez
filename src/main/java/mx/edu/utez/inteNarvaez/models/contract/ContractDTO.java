package mx.edu.utez.inteNarvaez.models.contract;
import lombok.*;
import java.util.Date;


@Data
@Getter
@Setter
@AllArgsConstructor

public class ContractDTO {

    private Double amount;
    private Long address;
    private String salesPackage;

    private Long userId;

    private Long id;



}
