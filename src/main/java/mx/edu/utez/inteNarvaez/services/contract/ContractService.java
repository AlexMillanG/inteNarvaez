package mx.edu.utez.inteNarvaez.services.contract;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.contract.ContractBean;
import mx.edu.utez.inteNarvaez.models.contract.ContractRepository;
import mx.edu.utez.inteNarvaez.models.contract.ContractValidation;
import mx.edu.utez.inteNarvaez.models.dtos.ResponseDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContractService {

    private final ContractRepository repository;
    private static final Logger logger = LogManager.getLogger(ContractService.class);

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAllContract() {
        try {
            List<ContractBean> contratos = repository.findAll();
            if (contratos.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(Collections.emptyList(),HttpStatus.NO_CONTENT, "No se encuentra ningún contrato registrado"),HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(new ApiResponse(contratos, HttpStatus.OK, "Lista de contratos"), HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al consultar los contratos", e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR,"Ocurrió un error al consultar los contratos"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> SaveContract(ContractBean contractBean) {

        ApiResponse validations = ContractValidation.validate(contractBean);

        if (validations.isError()) {
            return new ResponseEntity<>(validations, HttpStatus.BAD_REQUEST);
        }
        try {

            repository.save(contractBean);
            logger.info("Contrato creado exitosamente");
            return new ResponseEntity<>(new ApiResponse(contractBean, HttpStatus.CREATED, "Contrato creado exitosasmente"), HttpStatus.OK);

        } catch (DataIntegrityViolationException ex) {
            logger.error("Error de integridad al crear un contrato {}", ex.getMessage());
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "Confricto en los datos del contrato "), HttpStatus.CONFLICT);

        } catch (Exception ex) {
            logger.error("Error al crear un contrato {}", ex.getMessage());
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Algo salio mal al crear el contrato "), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> updateContract(ContractBean contractBean) {

        ApiResponse validation = ContractValidation.validate(contractBean);
        if (validation.isError()) {
            return new ResponseEntity<>(validation, HttpStatus.BAD_REQUEST);
        }
        try {

            Optional<ContractBean> findObj = repository.findAllByUuid(contractBean.getUuid());

            if (findObj.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "El contrato no existe"), HttpStatus.NOT_FOUND);
            }

            ContractBean existingContract = findObj.get();
            existingContract.setAmount(contractBean.getAmount());

            repository.saveAndFlush(existingContract);

            return new ResponseEntity<>(new ApiResponse(existingContract, HttpStatus.OK, "Actualización exitosa"), HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("Error al actualizar el contrato: {}", ex.getMessage(), ex);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Algo salió mal al actualizar el contrato"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }






}
