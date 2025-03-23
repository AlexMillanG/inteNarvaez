package mx.edu.utez.inteNarvaez.services.contract;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.address.AddressBean;
import mx.edu.utez.inteNarvaez.models.address.AddressRepository;
import mx.edu.utez.inteNarvaez.models.contract.ContractBean;
import mx.edu.utez.inteNarvaez.models.contract.ContractDTO;
import mx.edu.utez.inteNarvaez.models.contract.ContractRepository;
import mx.edu.utez.inteNarvaez.models.contract.ContractValidation;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageEntity;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@AllArgsConstructor
public class ContractService {

    private final ContractRepository repository;
    private  final SalesPackageRepository Salesrepository;
    private  final AddressRepository addressRepository;

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
    public ResponseEntity<ApiResponse> saveContract(ContractDTO dto) {

        /*
        ApiResponse validations = ContractValidation.validate(contractBean);
        if (validations.isError()) {
            return new ResponseEntity<>(validations, HttpStatus.BAD_REQUEST);
        }*/


        try {
            UUID addressUUID = UUID.fromString(dto.getAddress());
            UUID salesPackageUUID = UUID.fromString(dto.getSalesPackage());
            logger.info("addressUUID {}",addressUUID);
            logger.info("salesPackageUUID {}",salesPackageUUID);
            logger.info("Consultando la BD ");

            Optional<SalesPackageEntity> findSalepackage = Salesrepository.findByUuid(salesPackageUUID);
            Optional<AddressBean> findAddress = addressRepository.findByUuid(addressUUID);

            logger.info("Validaciones de exixtencias");

            if (findAddress.isEmpty()){
                logger.info("address OBJ {}",findAddress);
                return new ResponseEntity<>(new ApiResponse(findAddress.get(), HttpStatus.NOT_FOUND, "Direccion del cliente no encontrada",true), HttpStatus.NOT_FOUND);
            }


            if (findSalepackage.isEmpty()) {
                logger.info("salesPackage OBJ {}",findSalepackage);

                return new ResponseEntity<>(new ApiResponse(findSalepackage.get(), HttpStatus.NOT_FOUND, "Paquete de venta no encontrado",true), HttpStatus.NOT_FOUND);
            }

            logger.info("Creando obj");

            ContractBean contract = new ContractBean(
                    dto.getCreationDate(),
                    dto.getAmount(),
                    UUID.randomUUID(),
                    findAddress.get(),
                    findSalepackage.get()
            );

            ContractBean savedContract = repository.save(contract);

                  logger.info("Contrato creado exitosamente");


            return new ResponseEntity<>(new ApiResponse(savedContract, HttpStatus.CREATED, "Contrato creado exitosamente"), HttpStatus.CREATED);

        } catch (DataIntegrityViolationException ex) {
            logger.error("Error de integridad al crear un contrato: {}", ex.getMessage());
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "Conflicto en los datos del contrato"), HttpStatus.CONFLICT);

        } catch (Exception ex) {
            logger.error("Error al crear un contrato: {}", ex.getMessage());
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Algo salió mal al crear el contrato"), HttpStatus.INTERNAL_SERVER_ERROR);
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
