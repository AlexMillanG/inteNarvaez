package mx.edu.utez.inteNarvaez.controllers.salesPackages;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.salePackage.SalePackageDTO;
import mx.edu.utez.inteNarvaez.services.salesPackages.SalePackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/salesPackage/")
@AllArgsConstructor
public class SalePackageController {

    private final SalePackageService service;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllSalesPackage() {
        return service.findAllSalePackage();
    }

    @PostMapping("/save") public ResponseEntity<ApiResponse> save(@Valid @RequestBody SalePackageDTO dto , BindingResult result){
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}

        return  service.save(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        return service.delete(id);
    }

}
