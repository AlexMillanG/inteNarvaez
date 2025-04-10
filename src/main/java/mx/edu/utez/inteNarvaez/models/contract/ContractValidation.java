package mx.edu.utez.inteNarvaez.models.contract;

import mx.edu.utez.inteNarvaez.config.ApiResponse;
import org.springframework.http.HttpStatus;

public class ContractValidation {

    public static ApiResponse validate(ContractBean contract) {

        if (contract.getCreationDate() == null) {
            return new ApiResponse(null, HttpStatus.BAD_REQUEST, "El campo creationDate no puede ser nulo.", true);
        }

        if (contract.getAddress() == null) {
            return new ApiResponse(null, HttpStatus.BAD_REQUEST, "Addres no puede ser nulo.", true);
        }

        if (contract.getSalesPackageEntity() == null) {
            return new ApiResponse(null, HttpStatus.BAD_REQUEST, "SalesPackageEntity no puede ser nulo.", true);
        }

        return new ApiResponse(contract, HttpStatus.OK, "Validación exitosa.", false);
    }
}
