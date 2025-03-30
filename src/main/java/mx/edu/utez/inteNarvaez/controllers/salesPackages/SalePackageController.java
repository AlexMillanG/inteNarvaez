package mx.edu.utez.inteNarvaez.controllers.salesPackages;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.salePackage.SalePackageDTO;
import mx.edu.utez.inteNarvaez.services.salesPackages.SalePackageService;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/save") public ResponseEntity<ApiResponse> save(@RequestBody SalePackageDTO dto){
        return  service.save(dto);
    }

}
