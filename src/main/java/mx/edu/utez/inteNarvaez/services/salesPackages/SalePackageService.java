package mx.edu.utez.inteNarvaez.services.salesPackages;


import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageBean;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageRepository;
import mx.edu.utez.inteNarvaez.models.products.ProductBean;
import mx.edu.utez.inteNarvaez.models.products.ProductRepository;
import mx.edu.utez.inteNarvaez.models.salePackage.SalePackageDTO;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageEntity;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SalePackageService {
    private final SalesPackageRepository repository;
    private final ChannelPackageRepository channelPackageRepository;
    private final ProductRepository productRepository;
    private static final Logger logger = LogManager.getLogger(SalePackageService.class);

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> findAllSalePackage() {
        try {
            List<SalesPackageEntity> salePackages = repository.findAll();

            // Evitar inicializaciones forzadas y manejar relaciones de forma explícita.
            salePackages.forEach(sp -> Hibernate.initialize(sp.getChannelPackage().getChannels()));

            // Devolver la respuesta adecuada.
            return new ResponseEntity<>(
                    new ApiResponse(salePackages, HttpStatus.OK, "Paquetes obtenidos exitosamente", false),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            // Manejar errores explícitamente.
            logger.error("Error al obtener paquetes de ventas", e);
            return new ResponseEntity<>(
                    new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Algo salió mal", true),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> save(SalePackageDTO dto){

        try {
            logger.info("{} Nombre del paquete", dto.getName());
            Optional<SalesPackageEntity> findObjc = repository.findByName(dto.getName());

            if (findObjc.isPresent()){return new ResponseEntity<>(new ApiResponse(null,HttpStatus.FOUND,"Ya existe un paquete con ese nombre",true), HttpStatus.FOUND);}



            Optional<ChannelPackageBean> findChannelPackage = channelPackageRepository.findChannelPackageBeanByName(dto.getChannel_package_name());
            if (findChannelPackage.isEmpty()){return new ResponseEntity<>(new ApiResponse(null,HttpStatus.NOT_FOUND,"El paquete de canales no fue encontrado",true), HttpStatus.NOT_FOUND);}

            Optional<ProductBean> findProduct = productRepository.findProductBeanByName(dto.getProduct_name());
            if (findProduct.isEmpty()){return new ResponseEntity<>(new ApiResponse(null,HttpStatus.NOT_FOUND,"El producto no fue encontrado",true), HttpStatus.NOT_FOUND);}

            SalesPackageEntity salesPackage = new SalesPackageEntity(
                    dto.getName(),dto.getTotalAmount(),
                    UUID.randomUUID(), findChannelPackage.get(),
                    findProduct.get());

          SalesPackageEntity obj =  repository.saveAndFlush(salesPackage);
            return new ResponseEntity<>(new ApiResponse(obj,HttpStatus.CREATED,"Paquete creado exitosamente",false), HttpStatus.CREATED);

        }catch (Exception ex){
            logger.error("Algo salio mal",ex);
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.INTERNAL_SERVER_ERROR,"Algo salio mal"+ex,true), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}
