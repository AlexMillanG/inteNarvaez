package mx.edu.utez.inteNarvaez.services.salesPackages;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageBean;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageRepository;
import mx.edu.utez.inteNarvaez.models.products.ProductBean;
import mx.edu.utez.inteNarvaez.models.products.ProductRepository;
import mx.edu.utez.inteNarvaez.models.salePackage.SalePackageDTO;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageEntity;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageRepository;
import mx.edu.utez.inteNarvaez.services.contract.ContractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SalePackageService {
    private final SalesPackageRepository repository;
    private final ChannelPackageRepository channelPackageRepository;
    private final ProductRepository productRepository;
    private static final Logger logger = LogManager.getLogger(ContractService.class);
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAllSalePackage(){
        try {
            return new ResponseEntity<>(new ApiResponse(repository.findAll(),HttpStatus.INTERNAL_SERVER_ERROR,"Lista de packetes de ventas",false), HttpStatus.OK);
        }catch (Exception ex){

            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.INTERNAL_SERVER_ERROR,"Algo salio mal"+ex,true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> save(SalePackageDTO dto){

        try {
            logger.info(dto.getName() +" Nombre del paquete");
            Optional<SalesPackageEntity> findObjc = repository.findByName(dto.getName());

            if (findObjc.isPresent()){return new ResponseEntity<>(new ApiResponse(null,HttpStatus.FOUND,"Ya existe un paquete con ese nombre",true), HttpStatus.FOUND);}

            UUID channelPackageUUID =UUID.fromString( dto.getChannel_package_uuid());
            UUID productUUID =UUID.fromString( dto.getProduct_uuid());

            Optional<ChannelPackageBean> findChannelPackage = channelPackageRepository.findByUuid(channelPackageUUID);
            if (findChannelPackage.isEmpty()){return new ResponseEntity<>(new ApiResponse(null,HttpStatus.NOT_FOUND,"El paquete de canales no fue encontrado",true), HttpStatus.NOT_FOUND);}

            Optional<ProductBean> findProduct = productRepository.findByUuid(productUUID);
            if (findProduct.isEmpty()){return new ResponseEntity<>(new ApiResponse(null,HttpStatus.NOT_FOUND,"El producto no fue encontrado",true), HttpStatus.NOT_FOUND);}

            SalesPackageEntity salesPackage = new SalesPackageEntity(
                    dto.getName(),dto.getTotalAmount(),
                    UUID.randomUUID(), findChannelPackage.get(),
                    findProduct.get());

          SalesPackageEntity obj =  repository.saveAndFlush(salesPackage);
            return new ResponseEntity<>(new ApiResponse(obj,HttpStatus.INTERNAL_SERVER_ERROR,"Pakete Creado exitosamente",false), HttpStatus.OK);

        }catch (Exception ex){
            logger.error("Algo salio mal",ex);
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.INTERNAL_SERVER_ERROR,"Algo salio mal"+ex,true), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}
