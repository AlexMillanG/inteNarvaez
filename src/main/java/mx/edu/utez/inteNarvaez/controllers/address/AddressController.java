package mx.edu.utez.inteNarvaez.controllers.address;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.controllers.address.dto.AddressDTO;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageRepository;
import mx.edu.utez.inteNarvaez.services.address.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;


    @PostMapping("/save")
    public ResponseEntity<ApiResponse> save(@RequestBody AddressDTO dto) {
        return addressService.save(dto);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAll() {
        return addressService.findAll();
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<ApiResponse>findByUuid(@PathVariable UUID uuid) {
        return addressService.findByUuid(uuid);
    }

    @GetMapping("/find-by-client/{clientId}")
    public ResponseEntity<ApiResponse> findByClient(@PathVariable Long clientId) {
        return addressService.findByClientId(clientId);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> update(@RequestBody AddressDTO dto) {
        /*
                {
                    "id":1,
                    "name":"casa de alex UPDATE",
                    "street":"orquidea UPDATE",
                    "number":447,
                    "city":"Cuernavaca ",
                    "state":"morelos",
                    "zipCode":62460,
                    "clientId":1
                }
         */
        return addressService.update(dto.toEntity());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id){
        return addressService.deleteAddress(id);
    }


}
