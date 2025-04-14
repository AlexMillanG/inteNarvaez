package mx.edu.utez.inteNarvaez.services.client;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.address.AddressBean;
import mx.edu.utez.inteNarvaez.models.address.AddressRepository;
import mx.edu.utez.inteNarvaez.models.client.ClientBean;
import mx.edu.utez.inteNarvaez.models.client.ClientRepository;
import mx.edu.utez.inteNarvaez.services.contract.ContractService;
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


import static mx.edu.utez.inteNarvaez.services.channelCategory.ChannelCategoryService.capitalize;

@Service
@Transactional(rollbackFor = SQLException.class)
@AllArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;

    private static final Logger logger = LogManager.getLogger(ContractService.class);

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findAllClient() {
        try {
            List<ClientBean> clients = clientRepository.findByStatus(true);

            return ResponseEntity.ok(new ApiResponse(clients, HttpStatus.OK, "Lista de clientes con direcciones", false));
        } catch (Exception ex) {
            logger.error("Error al consultar los datos: ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al solicitar la información", true));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> saveClient(ClientBean clientBean) {


        try {
            Optional<ClientBean> foundRfc = clientRepository.findByRfc(clientBean.getRfc());
            if (foundRfc.isPresent()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya hay un usuario registrado con el RFC: " + clientBean.getRfc(), true));
            }

            Optional<ClientBean> foundEmail = clientRepository.findByEmail(clientBean.getEmail());

            if (foundEmail.isPresent()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya hay un usuario registrado con el correo: " + clientBean.getEmail(), true));
            }


            clientBean.setStatus(true);
            clientBean.setName(capitalize(clientBean.getName()));
            clientBean.setLastName(capitalize(clientBean.getLastName()));
            clientBean.setSurname(capitalize(clientBean.getSurname()));
            clientBean.setEmail(clientBean.getEmail().toLowerCase());
            clientBean.setUuid(UUID.randomUUID().toString());
            clientBean.setRfc(clientBean.getRfc().toUpperCase());

            ClientBean savedClient = clientRepository.saveAndFlush(clientBean);

            return ResponseEntity.ok(new ApiResponse(savedClient, HttpStatus.OK, "Cliente creado correctamente", false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al guardar el cliente", true));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> updateClient(ClientBean clientBean) {

        try {
            if (clientBean.getId() == null || clientBean.getId() <= 0) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id no puede ser nulo", true));
            }
            Optional<ClientBean> foundClient = clientRepository.findById(clientBean.getId());

            if (foundClient.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, HttpStatus.NOT_FOUND, "El cliente no exixte", true));
            }

            Optional<ClientBean> foundEmail = clientRepository.findByEmail(clientBean.getEmail());


            if (foundEmail.isPresent() && !foundEmail.get().getId().equals(clientBean.getId())) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya hay un usuario registrado con el correo: " + clientBean.getEmail(), true));
            }

            clientBean.setId(foundClient.get().getId());
            clientBean.setUuid(foundClient.get().getUuid());
            clientBean.setName(capitalize(clientBean.getName()));
            clientBean.setLastName(capitalize(clientBean.getLastName()));
            clientBean.setEmail(clientBean.getEmail().toLowerCase());
            clientBean.setRfc(clientBean.getRfc().toUpperCase().trim());
            clientBean.setSurname(capitalize(clientBean.getSurname()));

            return ResponseEntity.ok(new ApiResponse(clientRepository.saveAndFlush(clientBean), HttpStatus.OK, "Cliente actualizado correctamente", false));

        } catch (Exception ex) {
            logger.error("Error al guardar al cliente ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al guardar el cliente", true));

        }

    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByUUID(String uuid) {

        try {
            if (uuid == null) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El uuid no puede ser nulo", true));
            }

            Optional<ClientBean> foundClient = clientRepository.findByUuid(uuid.toString());

            if (foundClient.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El cliente no existe", true));
            }

            return ResponseEntity.ok(new ApiResponse(foundClient.get(), HttpStatus.OK, null, false));
        }catch (Exception e) {
            logger.error("Error al consultar los datos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al solicitar la información", true));
        }

    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> delete (Long id){
    try {

        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id no puede ser nulo", true));
        }
        Optional<ClientBean> foundClient = clientRepository.findById(id);

        if (foundClient.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El cliente no existe", true));
        }


        List<AddressBean> foundAddresses = addressRepository.findByStatusAndClient(true, foundClient.get());

        if (!foundAddresses.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "no se puede eliminar al cliente, tiene direcciones asociadas", true));
        }


        ClientBean clientBean = foundClient.get();
        clientBean.setStatus(false);
        clientRepository.saveAndFlush(clientBean);

        return ResponseEntity.ok(new ApiResponse(clientBean, HttpStatus.OK, "Cliente eliminado correctamente", false));
    }catch (Exception e) {
        logger.error("Error al eliminar el cliente: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al eliminar el cliente", true));
    }
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> clientCount(){
        try {
            return new ResponseEntity<>(new ApiResponse(clientRepository.countByStatus(true),HttpStatus.OK,null,false),HttpStatus.OK);
        }catch (Error e){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.OK,"hubo un error al hacer un conteo de canales disponibles",true),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
