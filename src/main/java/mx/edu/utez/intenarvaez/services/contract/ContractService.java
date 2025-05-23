package mx.edu.utez.intenarvaez.services.contract;

import lombok.AllArgsConstructor;
import mx.edu.utez.intenarvaez.config.ApiResponse;
import mx.edu.utez.intenarvaez.models.address.AddressBean;
import mx.edu.utez.intenarvaez.models.address.AddressRepository;
import mx.edu.utez.intenarvaez.models.client.ClientRepository;
import mx.edu.utez.intenarvaez.models.contract.ContractBean;
import mx.edu.utez.intenarvaez.models.contract.ContractDTO;
import mx.edu.utez.intenarvaez.models.contract.ContractRepository;
import mx.edu.utez.intenarvaez.models.salePackage.SalesPackageEntity;
import mx.edu.utez.intenarvaez.models.salePackage.SalesPackageRepository;
import mx.edu.utez.intenarvaez.models.user.UserEntity;
import mx.edu.utez.intenarvaez.models.user.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

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
                return new ResponseEntity<>(new ApiResponse(Collections.emptyList(), HttpStatus.NO_CONTENT, "No se encuentra ningún contrato registrado"), HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(new ApiResponse(contratos, HttpStatus.OK, "Lista de contratos"), HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al consultar los contratos", e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al consultar los contratos"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> saveContract(ContractDTO dto) {

        try {

            logger.info("Consultando la BD ");

            Optional<SalesPackageEntity> findSalepackage = Salesrepository.findByName(dto.getSalesPackage());
            Optional<AddressBean> findAddress = addressRepository.findById(dto.getAddress());
            Optional<UserEntity> user = userRepository.findById(dto.getUserId());
            logger.info("Validaciones de exixtencias");

            if (findAddress.isEmpty()) {
                logger.info("address OBJ {}", findAddress);
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Direccion del cliente no encontrada", true), HttpStatus.NOT_FOUND);
            }
            if (user.isEmpty() || !user.get().getStatus()) {
                logger.info("User {}", user);
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "El Agente no existe o se encuentra inactivo por el momento", true), HttpStatus.NOT_FOUND);
            }


            if (findSalepackage.isEmpty()) {
                logger.info("salesPackage OBJ {}", findSalepackage);

                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Paquete de venta no encontrado", true), HttpStatus.NOT_FOUND);
            }


            Optional<UserEntity> foundUser = userRepository.findById(dto.getUserId());

            if (foundUser.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Usuario no encontrado", true), HttpStatus.NOT_FOUND);
            }

            UserEntity agent = foundUser.get();

            if (!agent.getStatus()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "el usuario esta eliminado", true), HttpStatus.CONFLICT);
            }


            logger.info("Creando obj");

            ContractBean contract = new ContractBean();
            contract.setCreationDate(new Date());
            contract.setStatus(true);
            contract.setAddress(findAddress.get());
            contract.setUuid(UUID.randomUUID().toString());
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
    public ResponseEntity<ApiResponse> updateContract(ContractDTO dto) {

        try {
            logger.info("Consultando la BD para actualizar contrato");

            Optional<ContractBean> existingContract = repository.findById(dto.getId());

            if (existingContract.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Contrato no encontrado", true), HttpStatus.NOT_FOUND);
            }

            Optional<SalesPackageEntity> findSalepackage = Salesrepository.findByName(dto.getSalesPackage());
            Optional<AddressBean> findAddress = addressRepository.findById(dto.getAddress());
            Optional<UserEntity> foundUser = userRepository.findById(dto.getUserId());


            if (findAddress.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Dirección del cliente no encontrada", true), HttpStatus.NOT_FOUND);
            }

            if (findSalepackage.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Paquete de venta no encontrado", true), HttpStatus.NOT_FOUND);
            }

            if (foundUser.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Usuario no encontrado", true), HttpStatus.NOT_FOUND);
            }

            UserEntity agent = foundUser.get();
            if (!agent.getStatus()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "El usuario está eliminado", true), HttpStatus.CONFLICT);
            }

            ContractBean contract = existingContract.get();
            contract.setSalesPackageEntity(findSalepackage.get());
            contract.setAddress(findAddress.get());
            contract.setSalesAgent(agent);


            ContractBean updatedContract = repository.save(contract);

            logger.info("Contrato actualizado exitosamente");

            return new ResponseEntity<>(new ApiResponse(updatedContract, HttpStatus.OK, "Contrato actualizado exitosamente"), HttpStatus.OK);

        } catch (DataIntegrityViolationException ex) {
            logger.error("Error de integridad al actualizar el contrato: {}", ex.getMessage());
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "Conflicto en los datos del contrato"), HttpStatus.CONFLICT);

        } catch (Exception ex) {
            logger.error("Error al actualizar el contrato: {}", ex.getMessage());
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Algo salió mal al actualizar el contrato"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> findByAgent(Long id) {

        try {
            Optional<UserEntity> foundUser = userRepository.findById(id);
            if (foundUser.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Error agente no encontrado", true), HttpStatus.NOT_FOUND);
            }

            UserEntity user = foundUser.get();

            if (!user.getStatus()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "Error agente no encontrado", true), HttpStatus.CONFLICT);
            }

            List<ContractBean> foundContracts = repository.findBySalesAgentAndStatus(user, true);

            return new ResponseEntity<>(new ApiResponse(foundContracts, HttpStatus.OK, null, false), HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al obtener la contrato del agente: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener la contrato del agente", true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> findAllContractsByAgent(Long id) {

        try {
            Optional<UserEntity> foundUser = userRepository.findById(id);
            if (foundUser.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Agente no encontrado", true), HttpStatus.NOT_FOUND);
            }

            UserEntity user = foundUser.get();

            if (!user.getStatus()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "Error agente esta inactivo", true), HttpStatus.CONFLICT);
            }

            List<ContractBean> foundContracts = repository.findBySalesAgent_Id(user.getId());

            return new ResponseEntity<>(new ApiResponse(foundContracts, HttpStatus.OK, null, false), HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error : {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener los contratos del agente", true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }







    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> delete(Long id) {
        try {
            if (id == null || id <= 0) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id no puede ser nulo", true), HttpStatus.BAD_REQUEST);
            }

            Optional<ContractBean> foundContract = repository.findById(id);

            if (foundContract.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Error contrato no encontrado", true), HttpStatus.NOT_FOUND);
            }

            ContractBean contract = foundContract.get();

            contract.setStatus(false);

            repository.saveAndFlush(contract);

            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.OK, "Contrato eliminado correctamente", false), HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al eliminar la contrato del agente: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar la contrato del agente", true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> findByClient(Long id) {
        try {
            if (id == null || id <= 0) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id no puede ser nulo", true), HttpStatus.BAD_REQUEST);
            }
            if (!clientRepository.existsById(id)) {
                return new ResponseEntity<>(
                        new ApiResponse(null, HttpStatus.NOT_FOUND, "Error cliente no encontrado", true),
                        HttpStatus.NOT_FOUND);
            }

            List<ContractBean> contracts = repository.findContractsByClientId(id);

            return new ResponseEntity<>(
                    new ApiResponse(contracts, HttpStatus.OK, "Contratos encontrados", false),
                    HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("Error al consultar los contratos ", ex);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al consultar los contratos del cliente", true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse> countActiveContracts() {
        try {
            return new ResponseEntity<>(new ApiResponse(repository.countByStatus(true), HttpStatus.OK, null, false), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error de la consulta de los contratos: ", e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "hubo un error al hacer un conteo de contratos disponibles", true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse> getContractsWithSalesPackage(){
        try {
            return new ResponseEntity<>(new ApiResponse(getContractsWithOnlyTotal(),HttpStatus.OK,null,false),HttpStatus.OK);
        }catch (Exception e){
            logger.error("Error al consultar los contratos del cliente: ", e);
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.INTERNAL_SERVER_ERROR,"ocurrió un error inesperado al traer los contratos con su paquete de venta"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<ContractWithSalesPackagelDTO> getContractsWithOnlyTotal() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return repository.findAll().stream()
                .map(contract -> new ContractWithSalesPackagelDTO(
                        contract.getId(),
                        formatter.format(contract.getCreationDate()),
                        contract.isStatus(),
                        contract.getUuid(),
                        contract.getAddress(),
                        contract.getSalesPackageEntity().getTotalAmount()
                ))
                .toList();

    }

}





