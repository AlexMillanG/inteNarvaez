package mx.edu.utez.intenarvaez.services.address;

import lombok.AllArgsConstructor;
import mx.edu.utez.intenarvaez.config.ApiResponse;
import mx.edu.utez.intenarvaez.controllers.address.dto.AddressDTO;
import mx.edu.utez.intenarvaez.models.address.AddressBean;
import mx.edu.utez.intenarvaez.models.address.AddressRepository;
import mx.edu.utez.intenarvaez.models.client.ClientBean;
import mx.edu.utez.intenarvaez.models.client.ClientRepository;
import mx.edu.utez.intenarvaez.models.contract.ContractBean;
import mx.edu.utez.intenarvaez.models.contract.ContractRepository;
import mx.edu.utez.intenarvaez.services.contract.ContractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = SQLException.class)
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final ClientRepository clientRepository;
    private final ContractRepository contractRepository;

    private static final Logger logger = LogManager.getLogger(AddressService.class);

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findAll() {
        return new ResponseEntity<>(new ApiResponse(addressRepository.findByStatus(true), HttpStatus.OK, null, false), HttpStatus.OK);
    }

    @Transactional(rollbackFor = Exception.class)

    public ResponseEntity<ApiResponse> save(AddressDTO dto) {
        try {
            AddressBean addressBean = dto.toEntity();

            Optional<ClientBean> foundClient = clientRepository.findById(addressBean.getClient().getId());
            Optional<AddressBean> foundAddress = addressRepository.findByNameAndClient_Id(addressBean.getName(),addressBean.getClient().getId());

            if (foundAddress.isPresent()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya tienes una direccion con ese nombre", true), HttpStatus.BAD_REQUEST);
            }

            if (foundClient.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El cliente no existe", true), HttpStatus.BAD_REQUEST);
            }

            if (!foundClient.get().getStatus()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "ERROR, no se puede asignar una dirección a un cliente eliminado", true), HttpStatus.CONFLICT);
            }

            addressBean.setUuid(UUID.randomUUID().toString());
            addressBean.setStatus(true);
            return new ResponseEntity<>(new ApiResponse(addressRepository.save(addressBean), HttpStatus.OK, "dirección guardada correctamente", false), HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al guardar la dirección: ", e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), false), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByUuid(String uuid) {

        try {
            Optional<AddressBean> foundAddress = addressRepository.findByUuid(uuid);

            if (foundAddress.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "La dirección no existe", true), HttpStatus.NOT_FOUND);
            }

            if (foundAddress.get().getStatus()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "ERROR, esta dirección ha sido eliminada", true), HttpStatus.CONFLICT);
            }

            return new ResponseEntity<>(new ApiResponse(foundAddress.get(), HttpStatus.OK, null, false), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error de direccion: ", e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), false), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> update(AddressBean addressBean) {

        try {
            if (addressBean.getId() == null) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id no puede ser nulo", true), HttpStatus.BAD_REQUEST);
            }
            Optional<AddressBean> foundAddress = addressRepository.findById(addressBean.getId());

            if (foundAddress.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "La dirección no existe", true), HttpStatus.NOT_FOUND);
            }
            if (!foundAddress.get().getStatus()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "ERROR, no puede actulizar una dirección eliminada", true), HttpStatus.CONFLICT);

            }
            addressBean.setUuid(foundAddress.get().getUuid());

            Optional<ClientBean> foundClient = clientRepository.findById(addressBean.getClient().getId());

            if (foundClient.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El cliente no se econtro", true), HttpStatus.BAD_REQUEST);
            }

            if (!foundClient.get().getStatus()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "ERROR, no se puede asignar una dirección a un cliente eliminado", true), HttpStatus.CONFLICT);
            }

            addressBean.setUuid(foundAddress.get().getUuid());
            return new ResponseEntity<>(new ApiResponse(addressRepository.save(addressBean), HttpStatus.OK, "Dirección actualizada correctamente", false), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al actualizar la dirección: ", e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByClientId(Long clientId) {

        try {
            if (clientId == null) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "error, id no proporcionado"), HttpStatus.BAD_REQUEST);
            }
            Optional<ClientBean> foundClient = clientRepository.findById(clientId);

            if (foundClient.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "El cliente ingresado no existe", true), HttpStatus.NOT_FOUND);
            }

            if (!foundClient.get().getStatus()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "ERROR, el cliente ha sido eliminado", true), HttpStatus.CONFLICT);
            }

            if (addressRepository.findByStatusAndClient(true, foundClient.get()).isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "El cliente no tiene direcciones", true), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new ApiResponse(addressRepository.findByStatusAndClient(true, foundClient.get()), HttpStatus.OK, null, false), HttpStatus.OK);


        } catch (Exception e) {
            logger.error("Error al buscar direcciones por ID de cliente: ", e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), false), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> deleteAddress(Long id) {
        try {
            if (id == null) {
                return new ResponseEntity<>(new ApiResponse(null, null, "id no proporcionado"), HttpStatus.BAD_REQUEST);
            }

            Optional<AddressBean> foundAddress = addressRepository.findById(id);
            if (foundAddress.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "error, id no proporcionado", true), HttpStatus.NOT_FOUND);
            }

            List<ContractBean> foundContracts = contractRepository.findByAddress(foundAddress.get());

            if (!foundContracts.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "ERROR, no se puede eliminar una dirección con contratos", true), HttpStatus.CONFLICT);
            }

            AddressBean addressBean = foundAddress.get();
            addressBean.setStatus(false);

            return new ResponseEntity<>(new ApiResponse(addressRepository.saveAndFlush(addressBean), HttpStatus.OK, "dirección eliminada con exito", false), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al eliminar la dirección: ", e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), false), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById(Long id) {
        try {
            if (id == null) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST,  "id no incluido "), HttpStatus.BAD_REQUEST);
            }
            Optional<AddressBean> foundAddress = addressRepository.findById(id);

            if (foundAddress.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "no se encotró la dirección"), HttpStatus.NOT_FOUND);
            }

            if (foundAddress.get().getStatus()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "esta dirección esta eliminada"), HttpStatus.CONFLICT);
            }

            return new ResponseEntity<>(new ApiResponse(foundAddress.get(), HttpStatus.OK, null, false), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error el la busqueda: ", e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
