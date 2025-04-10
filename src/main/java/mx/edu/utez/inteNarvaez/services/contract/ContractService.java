package mx.edu.utez.inteNarvaez.services.contract;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.address.AddressBean;
import mx.edu.utez.inteNarvaez.models.address.AddressRepository;
import mx.edu.utez.inteNarvaez.models.client.ClientRepository;
import mx.edu.utez.inteNarvaez.models.contract.ContractBean;
import mx.edu.utez.inteNarvaez.models.contract.ContractDTO;
import mx.edu.utez.inteNarvaez.models.contract.ContractRepository;
import mx.edu.utez.inteNarvaez.models.contract.ContractValidation;
import mx.edu.utez.inteNarvaez.models.role.RoleBean;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageEntity;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageRepository;
import mx.edu.utez.inteNarvaez.models.user.UserEntity;
import mx.edu.utez.inteNarvaez.models.user.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContractService {

    private final ContractRepository repository;
    private final SalesPackageRepository Salesrepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    private static final Logger logger = LogManager.getLogger(ContractService.class);

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> findAllContract() {
        try {
            logger.info("Consultando la BD para contratos");
            List<ContractBean> contratos = repository.findByStatus(true);
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


        try {



            logger.info("Consultando la BD ");

            Optional<SalesPackageEntity> findSalepackage = Salesrepository.findByName(dto.getSalesPackage());
            Optional<AddressBean> findAddress = addressRepository.findById(dto.getAddress());

            logger.info("Validaciones de exixtencias");

            if (findAddress.isEmpty()){
                logger.info("address OBJ {}",findAddress);
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Direccion del cliente no encontrada",true), HttpStatus.NOT_FOUND);
            }


            if (findSalepackage.isEmpty()) {
                logger.info("salesPackage OBJ {}",findSalepackage);

                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Paquete de venta no encontrado",true), HttpStatus.NOT_FOUND);
            }


            Optional<UserEntity> foundUser = userRepository.findById(dto.getUserId());

            if (foundUser.isEmpty()){
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Usuario no encontrado",true), HttpStatus.NOT_FOUND);
            }

            UserEntity agent = foundUser.get();

            if (!agent.getStatus()){
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "el usuario esta eliminado",true), HttpStatus.CONFLICT);
            }

            


            logger.info("Creando obj");

            ContractBean contract = new ContractBean();

            contract.setStatus(true);
            contract.setAddress(findAddress.get());
            contract.setUuid(UUID.randomUUID());
            contract.setSalesPackageEntity(findSalepackage.get());
            contract.setSalesAgent(foundUser.get());

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

            repository.saveAndFlush(existingContract);

            return new ResponseEntity<>(new ApiResponse(existingContract, HttpStatus.OK, "Actualización exitosa"), HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("Error al actualizar el contrato: {}", ex.getMessage(), ex);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Algo salió mal al actualizar el contrato"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<ApiResponse> findByAgent(Long id){

        Optional<UserEntity> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Error agente no encontrado",true), HttpStatus.NOT_FOUND);
        }

        UserEntity user = foundUser.get();

        if (!user.getStatus()){
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "Error agente no encontrado",true), HttpStatus.CONFLICT);
        }

        List<ContractBean> foundContracts = repository.findBySalesAgentAndStatus(user,true);


        return new ResponseEntity<>(new ApiResponse(foundContracts, HttpStatus.OK, null,false), HttpStatus.OK);

    }

    public ResponseEntity<ApiResponse> delete(Long id){


        Optional<ContractBean> foundContract = repository.findById(id);

        if (foundContract.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Error contrato no encontrado",true), HttpStatus.NOT_FOUND);
        }

        ContractBean contract = foundContract.get();

        contract.setStatus(false);

        repository.saveAndFlush(contract);

        return new ResponseEntity<>(new ApiResponse(null, HttpStatus.OK, "Contrato eliminado correctamente",false), HttpStatus.OK);
    }


    public ResponseEntity<ApiResponse> findByClient(Long id) {

        Optional<UserEntity> foundUser = userRepository.findById(id);

        if (foundUser.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Error cliente no encontrado", true), HttpStatus.NOT_FOUND);
        }

        // Recuperar contratos del cliente
        List<ContractBean> contracts = getContractsByClientId(id);

        return new ResponseEntity<>(new ApiResponse(contracts, HttpStatus.OK, "Contratos encontrados", false), HttpStatus.OK);
    }

    public List<ContractBean> getContractsByClientId(Long id) {
        return clientRepository.findById(id)
                .map(client -> client.getAddresses().stream()
                        .flatMap(address -> address.getContracts().stream())
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

}
