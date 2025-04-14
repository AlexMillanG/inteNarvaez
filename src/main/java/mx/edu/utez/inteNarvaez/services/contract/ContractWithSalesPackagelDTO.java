package mx.edu.utez.inteNarvaez.services.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.inteNarvaez.models.address.AddressBean;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageEntity;
import mx.edu.utez.inteNarvaez.models.user.UserEntity;

import java.util.Date;
import java.util.UUID;

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
