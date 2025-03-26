package mx.edu.utez.inteNarvaez.controllers.client;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.client.ClientBean;
import mx.edu.utez.inteNarvaez.services.client.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveClient(@RequestBody ClientBean clientBean){
        /*
                {
                    "name":"Alvaro",
                    "lastName":"Diaz",
                    "surname":"no se",
                    "rfc":"AAAAA",
                    "email":"alvarito@rimasentretainment.com",
                    "phone":22222222,
                    "birthdate":"2004-06-10"
                }
         */
        return clientService.saveClient(clientBean);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAllClient(){
        return clientService.findAllClient();
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateClient(@RequestBody ClientBean clientBean){
        return clientService.updateClient(clientBean);
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<ApiResponse> findOneClient(@PathVariable UUID uuid){
        return clientService.findByUUID(uuid);
    }
}
