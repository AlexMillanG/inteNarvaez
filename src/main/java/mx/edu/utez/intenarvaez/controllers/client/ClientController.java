package mx.edu.utez.intenarvaez.controllers.client;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.utez.intenarvaez.config.ApiResponse;
import mx.edu.utez.intenarvaez.services.client.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveClient(@Valid @RequestBody ClientDTO dto, BindingResult result){
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}
        return clientService.saveClient(dto.toEntity());
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAllClient(){
        return clientService.findAllClient();
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateClient(@Valid @RequestBody ClientDTO dto, BindingResult result){
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}

        return clientService.updateClient(dto.toEntityUpdate());
    }
    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<ApiResponse> findOneClient(@PathVariable String uuid){
        return clientService.findByUUID(uuid);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteClient(@PathVariable Long id){
        return clientService.delete(id);
    }

    @GetMapping("/clientCount/")
    public ResponseEntity<ApiResponse> clientCount(){
        return clientService.clientCount();
    }
}
