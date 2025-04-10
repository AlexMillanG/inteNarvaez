package mx.edu.utez.inteNarvaez.controllers.contract;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.contract.ContractDTO;
import mx.edu.utez.inteNarvaez.services.contract.ContractService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contract")
@AllArgsConstructor
public class ContractController {

    private final ContractService service;
    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAllContract(){
        return service.findAllContract();
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveContract(@RequestBody ContractDTO dto) {

        return service.saveContract(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteContract(@PathVariable Long id) {
        return service.delete(id);
    }





}
