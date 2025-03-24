package mx.edu.utez.inteNarvaez.controllers.address;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.controllers.address.dto.AddressDTO;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageRepository;
import mx.edu.utez.inteNarvaez.services.address.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final SalesPackageRepository repository;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> save(@RequestBody AddressDTO dto) {
        return addressService.save(dto);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAll() {
        return addressService.findAll();
    }
    @GetMapping("/sales")
    public ResponseEntity<ApiResponse> findAllSales() {
        return new ResponseEntity<>(new ApiResponse(repository.findAll(), HttpStatus.OK,""),HttpStatus.OK);
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
