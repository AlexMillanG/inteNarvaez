package mx.edu.utez.inteNarvaez.services.products;


import lombok.RequiredArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.products.ProductBean;
import mx.edu.utez.inteNarvaez.models.products.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = SQLException.class)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findAll(){
        return ResponseEntity.ok(new ApiResponse(productRepository.findAll(), HttpStatus.OK, "Lista de productos", false));
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> saveProduct(ProductBean productBean){

        if (productBean.getId() != null){
            productBean.setId(null);
        }

        if (productBean.getName().equals("") || productBean.getName() == null) {
            return ResponseEntity.ok(new ApiResponse(null, HttpStatus.BAD_REQUEST,"El nombre del producto es obligatorio",true ));
        }

        if (productBean.getSpeed().equals("") || productBean.getSpeed() == null) {
            return ResponseEntity.ok(new ApiResponse(null, HttpStatus.BAD_REQUEST,"La velocidad del producto es obligatoria",true ));
        }

        if (productBean.getAmount() == null) {
            return ResponseEntity.ok(new ApiResponse(null, HttpStatus.BAD_REQUEST,"El monto del producto es obligatorio",true ));
        }

        if (productBean.getDescription().equals("") || productBean.getDescription() == null) {
            return ResponseEntity.ok(new ApiResponse(null, HttpStatus.BAD_REQUEST,"La descripción del producto es obligatoria",true ));
        }

        return ResponseEntity.ok(new ApiResponse(productRepository.save(productBean), HttpStatus.OK, "Producto guardado correctamente", false));

    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> updateProduct(ProductBean productBean){

        if (productBean.getId() == null) {
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.BAD_REQUEST,"El id del producto es obligatorio",true),HttpStatus.BAD_REQUEST);
        }

        Optional<ProductBean> foundProduct = productRepository.findById(productBean.getId());

        if(foundProduct.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.BAD_REQUEST,"Error, el producto que intentas actualizar no existe",true),HttpStatus.BAD_REQUEST);
        }

        if (productBean.getName().equals("") || productBean.getName() == null) {
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.BAD_REQUEST,"El nombre del producto es obligatorio",true),HttpStatus.BAD_REQUEST);
        }

        if (productBean.getSpeed().equals("") || productBean.getSpeed() == null) {
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.BAD_REQUEST,"La velocidad del producto es obligatoria",true),HttpStatus.BAD_REQUEST);
        }

        if (productBean.getAmount() == null) {
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.BAD_REQUEST,"El monto del producto es obligatorio",true),HttpStatus.BAD_REQUEST);
        }

        if (productBean.getDescription().equals("") || productBean.getDescription() == null) {
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.BAD_REQUEST,"La descripción del producto es obligatoria",true),HttpStatus.BAD_REQUEST);
        }

        productBean.setUuid(foundProduct.get().getUuid());

        return ResponseEntity.ok(new ApiResponse(productRepository.save(productBean), HttpStatus.OK, "Producto guardado correctamente", false));

    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByUuid(UUID uuid){
        return ResponseEntity.ok(new ApiResponse(productRepository.findByUuid(uuid), HttpStatus.OK, "Producto encontrado", false));
    }
}
