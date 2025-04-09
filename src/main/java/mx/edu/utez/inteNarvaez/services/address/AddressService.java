package mx.edu.utez.inteNarvaez.services.address;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.controllers.address.dto.AddressDTO;
import mx.edu.utez.inteNarvaez.models.address.AddressBean;
import mx.edu.utez.inteNarvaez.models.address.AddressRepository;
import mx.edu.utez.inteNarvaez.models.client.ClientBean;
import mx.edu.utez.inteNarvaez.models.client.ClientRepository;
import mx.edu.utez.inteNarvaez.models.contract.ContractBean;
import mx.edu.utez.inteNarvaez.models.contract.ContractRepository;
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

@Service
@Transactional(rollbackFor = SQLException.class)
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final ClientRepository clientRepository;
    private final ContractRepository contractRepository;

    private static final Logger logger = LogManager.getLogger(ContractService.class);

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findAll() {
        return new ResponseEntity<>(new ApiResponse(addressRepository.findByStatus(true), HttpStatus.OK, null, false), HttpStatus.OK);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> save(AddressDTO dto) {

        AddressBean addressBean = dto.toEntity();

        if (addressBean.getName().equals("") || addressBean.getName() == null) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El nombre de la dirección no puede ser nulo o vacío", true), HttpStatus.BAD_REQUEST);
        }

        if (addressBean.getStreet().equals("") || addressBean.getStreet() == null) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La calle no puede ser nula o vacía", true), HttpStatus.BAD_REQUEST);
        }

        if (addressBean.getState().equals("") || addressBean.getState() == null) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El estado no puede ser nulo o vacío", true), HttpStatus.BAD_REQUEST);
        }

        if (addressBean.getZipCode() == null) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El código postal no puede ser nulo", true), HttpStatus.BAD_REQUEST);
        }

        if (addressBean.getNumber() == null) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El número no puede ser nulo", true), HttpStatus.BAD_REQUEST);
        }

        Optional<ClientBean> foundClient = clientRepository.findById(addressBean.getClient().getId());

        if (foundClient.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El cliente no existe", true), HttpStatus.BAD_REQUEST);
        }

        if (!foundClient.get().getStatus()){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.CONFLICT,"ERROR, no se puede asignar una dirección a un cliente eliminado",true), HttpStatus.CONFLICT);
        }

        addressBean.setUuid(UUID.randomUUID().toString());
        addressBean.setStatus(true);

        return new ResponseEntity<>(new ApiResponse(addressRepository.save(addressBean), HttpStatus.OK, "dirección guardada correctamente", false), HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByUuid(UUID uuid){
        Optional<AddressBean> foundAddress = addressRepository.findByUuid(uuid.toString());

        if (foundAddress.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.NOT_FOUND,"La dirección no existe",true), HttpStatus.NOT_FOUND);
        }

        if (foundAddress.get().getStatus()){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.CONFLICT,"ERROR, esta dirección ha sido eliminada",true), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(new ApiResponse(foundAddress.get(),HttpStatus.OK,null,false), HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> update(AddressBean addressBean){
        Optional<AddressBean> foundAddress = addressRepository.findById(addressBean.getId());


        if (foundAddress.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.NOT_FOUND,"La dirección no existe",true), HttpStatus.NOT_FOUND);
        }

        if (!foundAddress.get().getStatus()){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.CONFLICT,"ERROR, no puede actulizar una dirección eliminada",true), HttpStatus.CONFLICT);

        }

        addressBean.setUuid(foundAddress.get().getUuid());

        if (addressBean.getCity().equals("") || addressBean.getCity() == null){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.BAD_REQUEST,"La ciudad no puede ser nula o vacía",true), HttpStatus.BAD_REQUEST);
        }

        if (addressBean.getName().equals("") || addressBean.getName() == null){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.BAD_REQUEST,"El nombre de la dirección no puede ser nulo o vacío",true), HttpStatus.BAD_REQUEST);
        }

        if (addressBean.getStreet().equals("") || addressBean.getStreet() == null){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.BAD_REQUEST,"La calle no puede ser nula o vacía",true), HttpStatus.BAD_REQUEST);
        }

        if (addressBean.getState().equals("") || addressBean.getState() == null){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.BAD_REQUEST,"El estado no puede ser nulo o vacío",true), HttpStatus.BAD_REQUEST);
        }

        if (addressBean.getZipCode() == null){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.BAD_REQUEST,"El código postal no puede ser nulo",true), HttpStatus.BAD_REQUEST);
        }

        if (addressBean.getNumber() == null){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.BAD_REQUEST,"El número no puede ser nulo",true), HttpStatus.BAD_REQUEST);
        }

        Optional<ClientBean> foundClient = clientRepository.findById(addressBean.getClient().getId());

        if (foundClient.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.BAD_REQUEST,"El cliente no existe",true), HttpStatus.BAD_REQUEST);
        }

        if (!foundClient.get().getStatus()){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.CONFLICT,"ERROR, no se puede asignar una dirección a un cliente eliminado",true), HttpStatus.CONFLICT);
        }


        addressBean.setUuid(foundAddress.get().getUuid());

        return new ResponseEntity<>(new ApiResponse(addressRepository.save(addressBean),HttpStatus.OK,"Dirección actualizada correctamente",false), HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByClientId(Long clientId){
        Optional<ClientBean> foundClient = clientRepository.findById(clientId);

        if (foundClient.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.NOT_FOUND,"El cliente no existe",true), HttpStatus.NOT_FOUND);
        }

        if (!foundClient.get().getStatus()){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.CONFLICT,"ERROR, el cliente ha sido eliminado",true), HttpStatus.CONFLICT);
        }

        if (addressRepository.findByStatusAndClient(true,foundClient.get()).isEmpty()){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.NOT_FOUND,"El cliente no tiene direcciones",true), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ApiResponse(addressRepository.findByStatusAndClient(true,foundClient.get()),HttpStatus.OK,null,false), HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> deleteAddress(Long id){
        if (id == null){
            return new ResponseEntity<>(new ApiResponse(null,null,"error, id no proporcionado"),HttpStatus.BAD_REQUEST);
        }

        Optional<AddressBean> foundAddress = addressRepository.findById(id);
        if (foundAddress.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.NOT_FOUND,"error, id no proporcionado",true),HttpStatus.NOT_FOUND);
        }


        List<ContractBean> foundContracts = contractRepository.findByAddress(foundAddress.get());

        if (!foundContracts.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.CONFLICT,"ERROR, no se puede eliminar una dirección con contratos",true), HttpStatus.CONFLICT);
        }


        AddressBean addressBean = foundAddress.get();
        addressBean.setStatus(false);

        return new ResponseEntity<>(new ApiResponse(addressRepository.saveAndFlush(addressBean),HttpStatus.OK,"dirección eliminada con exito",false),HttpStatus.OK);
    }
}
