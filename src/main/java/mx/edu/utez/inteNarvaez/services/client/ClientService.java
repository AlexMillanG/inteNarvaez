package mx.edu.utez.inteNarvaez.services.client;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.client.ClientBean;
import mx.edu.utez.inteNarvaez.models.client.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;





import static mx.edu.utez.inteNarvaez.services.channelCategory.ChannelCategoryService.capitalize;

@Service
@Transactional(rollbackFor = SQLException.class)
@AllArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> saveClient(ClientBean clientBean) {

        if (StringUtils.isBlank(clientBean.getName())) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El nombre del cliente no puede ser nulo o vacío", true));
        }

        if (StringUtils.isBlank(clientBean.getLastName())) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El apellido del cliente no puede ser nulo o vacío", true));
        }

        if (StringUtils.isBlank(clientBean.getSurname())) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El apellido materno del cliente no puede ser nulo o vacío", true));
        }

        if (StringUtils.isBlank(clientBean.getPhone())) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El teléfono del cliente no puede ser nulo o vacío", true));
        }

        if (StringUtils.isBlank(clientBean.getEmail())) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El correo del cliente no puede ser nulo o vacío", true));
        }



        clientBean.setName(capitalize(clientBean.getName()));
        clientBean.setLastName(capitalize(clientBean.getLastName()));
        clientBean.setSurname(capitalize(clientBean.getSurname()));
        clientBean.setEmail(clientBean.getEmail().toLowerCase());
        clientBean.setRfc(clientBean.getRfc().toUpperCase());

        Optional<ClientBean> foundRfc = clientRepository.findByRfc(clientBean.getRfc());
        if (foundRfc.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya hay un usuario registrado con el RFC: " + clientBean.getRfc(), true));
        }

        return ResponseEntity.ok(new ApiResponse(clientRepository.save(clientBean), HttpStatus.OK, "Cliente creado correctamente", false));
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findAllClient(){
        return ResponseEntity.ok(new ApiResponse(clientRepository.findAll(), HttpStatus.OK, null, false));
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> updateClient(ClientBean clientBean){
        Optional<ClientBean> foundClient = clientRepository.findById(clientBean.getId());

        if (foundClient.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El cliente no existe", true));
        }

        if (clientBean.getName().equals("") || clientBean.getName() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El nombre del cliente no puede ser nulo", true));
        }

        if (clientBean.getLastName().equals("") || clientBean.getLastName() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El apellido del cliente no puede ser nulo", true));
        }

        if(clientBean.getSurname().equals("") || clientBean.getSurname() == null){
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El apellido materno del cliente no puede ser nulo", true));
        }

        if (clientBean.getPhone().equals("") || clientBean.getPhone() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El teléfono del cliente no puede ser nulo", true));
        }

        if (clientBean.getEmail().equals("") || clientBean.getEmail() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El correo del cliente no puede ser nulo", true));
        }

        if (clientBean.getBirthdate().equals("") || clientBean.getBirthdate() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La fecha de nacimiento", true));

        }

        clientBean.setUuid(foundClient.get().getUuid());
        clientBean.setName(capitalize(clientBean.getName()));
        clientBean.setLastName(capitalize(clientBean.getLastName()));
        clientBean.setEmail(clientBean.getEmail().toLowerCase());
        clientBean.setRfc(clientBean.getRfc().toUpperCase().trim());
        clientBean.setSurname(capitalize(clientBean.getSurname()));

        return ResponseEntity.ok(new ApiResponse(clientRepository.save(clientBean), HttpStatus.OK, "Cliente actualizado correctamente", false));
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByUUID(UUID uuid){
        Optional<ClientBean> foundClient = clientRepository.findByUuid(uuid);

        if (foundClient.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El cliente no existe", true));
        }

        return ResponseEntity.ok(new ApiResponse(foundClient.get(), HttpStatus.OK, null, false));
    }



}


