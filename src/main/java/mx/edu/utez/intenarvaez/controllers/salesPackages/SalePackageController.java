package mx.edu.utez.intenarvaez.controllers.salesPackages;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mx.edu.utez.intenarvaez.config.ApiResponse;
import mx.edu.utez.intenarvaez.models.salePackage.SalePackageDTO;
import mx.edu.utez.intenarvaez.services.salesPackages.SalePackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/salesPackage/")
@AllArgsConstructor
public class SalePackageController {

    private final SalePackageService service;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllSalesPackageActive() {
        return service.findAllSalePackage();
    }


    @PostMapping("/save") public ResponseEntity<ApiResponse> save(@Valid @RequestBody SalePackageDTO dto , BindingResult result){
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}
        return  service.save(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> update(@Valid @RequestBody SalePackageDTO dto , BindingResult result){
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}
        return service.update(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        return service.delete(id,1L);
    }

    @GetMapping("/active/{id}")
    public ResponseEntity<ApiResponse> active(@PathVariable Long id) {
        return service.delete(id,2L);
    }


    @GetMapping("/salesPackageCount/")
    public ResponseEntity<ApiResponse> countSalesPackages(){
        return service.countSalesPackages();
    }

}
