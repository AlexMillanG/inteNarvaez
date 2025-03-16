package mx.edu.utez.inteNarvaez.models.contract;

import mx.edu.utez.inteNarvaez.config.ApiResponse;
import org.springframework.http.HttpStatus;

public class ContractValidation {

    public static ApiResponse validate(ContractBean contract) {

        if (contract.getCreationDate() == null) {
            return new ApiResponse(null, HttpStatus.BAD_REQUEST, "El campo creationDate no puede ser nulo.", true);
        }

        if (contract.getAmount() == null || contract.getAmount() <= 0) {
            return new ApiResponse(null, HttpStatus.BAD_REQUEST, "El campo amount no puede ser nulo y debe ser mayor a 0.", true);
        }

        if (contract.getAddress() == null) {
            return new ApiResponse(null, HttpStatus.BAD_REQUEST, "El campo address no puede ser nulo.", true);
        }

        if (contract.getSalesPackageEntity() == null) {
            return new ApiResponse(null, HttpStatus.BAD_REQUEST, "El campo salesPackageEntity no puede ser nulo.", true);
        }

        return new ApiResponse(contract, HttpStatus.OK, "Validación exitosa.", false);
    }
}
