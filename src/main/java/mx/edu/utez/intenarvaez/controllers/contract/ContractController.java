package mx.edu.utez.intenarvaez.controllers.contract;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mx.edu.utez.intenarvaez.config.ApiResponse;
import mx.edu.utez.intenarvaez.models.contract.ContractDTO;
import mx.edu.utez.intenarvaez.services.contract.ContractService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<ApiResponse> saveContract(@Valid @RequestBody ContractDTO dto , BindingResult result) {
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}
        return service.saveContract(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> UpdateContract(@Valid @RequestBody ContractDTO dto , BindingResult result) {
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}
        return service.updateContract(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteContract(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping("/findByClient/{id}")
    public ResponseEntity<ApiResponse> findByClient(@PathVariable Long id) {
        return service.findByClient(id);
    }

    @GetMapping("/findByAgent/{id}")
    public ResponseEntity<ApiResponse> findByAgent(@PathVariable Long id){
        return service.findByAgent(id);
    }

    @GetMapping("/findContractsAgent/{id}")
    public ResponseEntity<ApiResponse> findAllContractsByAgent(@PathVariable Long id){
        return service.findAllContractsByAgent(id);
    }

    @GetMapping("/contractsCount/")
    public ResponseEntity<ApiResponse> contractsCounts(){
        return service.countActiveContracts();
    }

    @GetMapping("/contracts/with-total")
    public ResponseEntity<ApiResponse> getContractsWithTotal() {
        return service.getContractsWithSalesPackage();
    }





}
