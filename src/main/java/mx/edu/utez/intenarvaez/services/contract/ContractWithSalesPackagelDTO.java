package mx.edu.utez.intenarvaez.services.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.intenarvaez.models.address.AddressBean;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractWithSalesPackagelDTO {
    private Long id;
    private String creationDate;
    private boolean status;
    private String uuid;
    private AddressBean address;

    private Double totalAmount;
}
