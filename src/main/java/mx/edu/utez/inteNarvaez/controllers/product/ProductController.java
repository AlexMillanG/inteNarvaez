package mx.edu.utez.inteNarvaez.controllers.product;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.controllers.product.dto.ProductDTO;
import mx.edu.utez.inteNarvaez.services.products.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAll(){
        return productService.findAll();
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveProduct(@RequestBody ProductDTO productDTO){
        return productService.saveProduct(productDTO.toEntity());

        /*
                   {
                    "name": "internet 1",
                    "speed":"22MB",
                    "description":"es un internet asi bien cabronsote",
                    "amount":444.00
                   }
        */
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductDTO productDTO){
        return productService.updateProduct(productDTO.toEntity());
        /*
               {
                "id":2,
                "name": "internet 1 UPDATE",
                "speed":"223MB",
                "description":"es un internet asi bien cabronsote UPDATE",
                "amount":44444.00
               }
         */
    }

    @GetMapping("/findByUuid/{uuid}")
    public ResponseEntity<ApiResponse> findByUuid(@PathVariable UUID uuid){
        return productService.findByUuid(uuid);
    }
}
