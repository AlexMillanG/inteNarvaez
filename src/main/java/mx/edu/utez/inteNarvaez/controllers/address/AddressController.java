package mx.edu.utez.inteNarvaez.controllers.address;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.controllers.address.dto.AddressDTO;
import mx.edu.utez.inteNarvaez.services.address.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.UUID;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor

public class AddressController {

    private final AddressService addressService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> save(@Valid @RequestBody AddressDTO dto, BindingResult result) {
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}

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

    @GetMapping("findOne/{id}")
    public ResponseEntity<ApiResponse> findById(@PathVariable Long id){
        return addressService.findById(id);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> update(@Valid @RequestBody AddressDTO dto, BindingResult result) {
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}

        return addressService.update(dto.toEntity());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id){
        return addressService.deleteAddress(id);
    }


}
