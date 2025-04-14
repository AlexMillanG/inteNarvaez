package mx.edu.utez.inteNarvaez.services.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.inteNarvaez.models.address.AddressBean;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractWithSalesPackagelDTO {
    private Long id;
    private String creationDate; // cambio aquí
    private boolean status;
    private UUID uuid;
    private AddressBean address;

    private Double totalAmount;
}
