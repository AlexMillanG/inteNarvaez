package mx.edu.utez.intenarvaez.services.salesPackages;


import lombok.AllArgsConstructor;
import mx.edu.utez.intenarvaez.config.ApiResponse;
import mx.edu.utez.intenarvaez.models.channelPackage.ChannelPackageBean;
import mx.edu.utez.intenarvaez.models.channelPackage.ChannelPackageRepository;
import mx.edu.utez.intenarvaez.models.channelPackage.ChannelPackageStatus;
import mx.edu.utez.intenarvaez.models.salePackage.SalePackageDTO;
import mx.edu.utez.intenarvaez.models.salePackage.SalesPackageEntity;
import mx.edu.utez.intenarvaez.models.salePackage.SalesPackageRepository;
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
    private static final Logger logger = LogManager.getLogger(SalePackageService.class);

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> findAllSalePackage() {
        try {
            List<SalesPackageEntity> salePackages = repository.findByStatus(true);

            salePackages.forEach(sp -> Hibernate.initialize(sp.getChannelPackage().getChannels()));

            return new ResponseEntity<>(
                    new ApiResponse(salePackages, HttpStatus.OK, "Paquetes obtenidos exitosamente", false),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            logger.error("Error al obtener paquetes de ventas", e);
            return new ResponseEntity<>(
                    new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error :", true),
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

            Optional<ChannelPackageBean> findChannelPackage = channelPackageRepository.findChannelPackageBeanByNameAndStatus(dto.getChannel_package_name(),ChannelPackageStatus.DISPONIBLE);
            if (findChannelPackage.isEmpty()){return new ResponseEntity<>(new ApiResponse(null,HttpStatus.NOT_FOUND,"El paquete de canales no fue encontrado",true), HttpStatus.NOT_FOUND);}


            if (findChannelPackage.get().getStatus() != ChannelPackageStatus.DISPONIBLE) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.FORBIDDEN, "El paquete de canales no está disponible", true), HttpStatus.FORBIDDEN);
            }

            SalesPackageEntity salesPackage = new SalesPackageEntity();

            salesPackage.setStatus(true);
            salesPackage.setName(dto.getName());
            salesPackage.setTotalAmount(dto.getTotalAmount());
            salesPackage.setUuid(UUID.randomUUID().toString());
            salesPackage.setChannelPackage(findChannelPackage.get());
            salesPackage.setSpeed(dto.getSpeed());

          SalesPackageEntity obj =  repository.saveAndFlush(salesPackage);
            return new ResponseEntity<>(new ApiResponse(obj,HttpStatus.CREATED,"Paquete creado exitosamente",false), HttpStatus.CREATED);

        }catch (Exception ex){
            logger.error("Algo salio mal",ex);
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.INTERNAL_SERVER_ERROR,"No se completo la accion error:"+ex,true), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> update(SalePackageDTO dto) {
        try {
            if (dto.getId() == null || dto.getId() <= 0 ) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id no puede ser nullo", true), HttpStatus.BAD_REQUEST);
            }

            Optional<SalesPackageEntity> findObjc = repository.findById(dto.getId());
            if (findObjc.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "No se encontró el paquete de ventas", true), HttpStatus.NOT_FOUND);
            }

            Optional<ChannelPackageBean> findChannelPackage = channelPackageRepository.findChannelPackageBeanByNameAndStatus(dto.getChannel_package_name(), ChannelPackageStatus.DISPONIBLE);
            if (findChannelPackage.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "El paquete de canales no fue encontrado", true), HttpStatus.NOT_FOUND);
            }


            SalesPackageEntity salesPackage = findObjc.get();
            if (!findObjc.get().getName().equals(dto.getName())) {

                Optional<SalesPackageEntity> findSales = repository.findByName(dto.getName());
                if (findSales.isPresent()) {
                    return new ResponseEntity<>(new ApiResponse(null, HttpStatus.FOUND, "Ya existe otro paquete de ventas con ese nombre", true), HttpStatus.FOUND);
                }
                salesPackage.setName(dto.getName());
            }else {
                salesPackage.setName(findObjc.get().getName());
            }

            salesPackage.setTotalAmount(dto.getTotalAmount());
            salesPackage.setSpeed(dto.getSpeed());
            salesPackage.setChannelPackage(findChannelPackage.get());

            SalesPackageEntity obj = repository.saveAndFlush(salesPackage);
            return new ResponseEntity<>(new ApiResponse(obj, HttpStatus.OK, "Paquete actualizado exitosamente", false), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al actualizar el paquete de ventas", e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Algo salió mal", true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> delete(Long id) {
        try {
            Optional<SalesPackageEntity> findObjc = repository.findById(id);
            if (findObjc.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "No se encontró el paquete de ventas", true), HttpStatus.NOT_FOUND);
            }

            int contractCount = repository.countContractsBySalesPackageId(id);
            if (contractCount > 0) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.FORBIDDEN, "No se puede eliminar el paquete de ventas porque tiene contratos asociados", true), HttpStatus.FORBIDDEN);
            }

            SalesPackageEntity salesPackage = findObjc.get();
            salesPackage.setStatus(false);
            repository.save(salesPackage);

            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.OK, "Paquete eliminado exitosamente", false), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al eliminar el paquete de ventas", e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Algo salió mal", true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse>countSalesPackages(){
        try {
            return new ResponseEntity<>(new ApiResponse(repository.countByStatus(true),HttpStatus.OK,null,false),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.INTERNAL_SERVER_ERROR,"hubo un error al consultar el conteo de paquetes de ventas",true),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
