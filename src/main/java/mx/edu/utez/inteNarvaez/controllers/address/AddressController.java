package mx.edu.utez.inteNarvaez.controllers.address;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.controllers.address.dto.AddressDTO;
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
        System.err.println(dto.getClientId());
        return addressService.save(dto.toEntity());
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
        return addressService.update(dto.toEntity());
    }


}
