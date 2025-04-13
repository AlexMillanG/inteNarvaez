package mx.edu.utez.inteNarvaez.services.channelPackage;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;
import mx.edu.utez.inteNarvaez.models.channel.ChannelRepository;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageBean;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageRepository;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageStatus;
import mx.edu.utez.inteNarvaez.models.contract.ContractRepository;
import mx.edu.utez.inteNarvaez.services.channelCategory.ChannelCategoryService;
import mx.edu.utez.inteNarvaez.services.email.EmailService;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageEntity;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = SQLException.class)
@RequiredArgsConstructor
public class ChannelPackageService {

    private final ChannelPackageRepository channelPackageRepository;
    private final ChannelRepository channelRepository;
    private final EmailService emailService;
    private final ContractRepository contractRepository;
    private final SalesPackageRepository salesPackageRepository;
    private static final Logger logger = LogManager.getLogger(ChannelCategoryService.class);


    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> save(ChannelPackageBean channelPackageBean) {
        try {
            Optional<ChannelPackageBean> foundPackageName = channelPackageRepository.findChannelPackageBeanByNameAndStatus(channelPackageBean.getName(), ChannelPackageStatus.DISPONIBLE);

            if (foundPackageName.isPresent()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: Ya existe un paquete de canales con el nombre " + channelPackageBean.getName(), true), HttpStatus.BAD_REQUEST);
            }

            if (channelPackageBean.getName() == null || channelPackageBean.getName().isBlank()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: ingresa un nombre válido", true), HttpStatus.BAD_REQUEST);
            }

            if (channelPackageBean.getDescription() == null || channelPackageBean.getDescription().isBlank()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: ingresa una descripción válida", true), HttpStatus.BAD_REQUEST);
            }


            if (channelPackageBean.getChannels() == null || channelPackageBean.getChannels().isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: el paquete debe contener al menos un canal", true), HttpStatus.BAD_REQUEST);
            }

            for (ChannelBean channelBean : channelPackageBean.getChannels()) {
                Optional<ChannelBean> foundChannel = channelRepository.findById(channelBean.getId());
                if (foundChannel.isEmpty()) {
                    return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Error: el canal con ID " + channelBean.getId() + " no existe", true), HttpStatus.NOT_FOUND);
                }

                if (!foundChannel.get().getStatus()) {
                    return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "ERROR, no puedes asignar canales eliminados, canal eliminado: " + foundChannel.get().getName(), true), HttpStatus.CONFLICT);
                }
            }

            channelPackageBean.setStatus(ChannelPackageStatus.DISPONIBLE);
            ChannelPackageBean savedPackage = channelPackageRepository.save(channelPackageBean);
            return new ResponseEntity<>(new ApiResponse(savedPackage, HttpStatus.CREATED, "Paquete guardado correctamente", false), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el paquete de canales: " + e.getMessage(), true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> update(ChannelPackageBean channelPackageBean) {
        try {

            for (ChannelBean channelBean : channelPackageBean.getChannels()) {
                System.err.println("id del paquete de canal " + channelBean.getId());
            }

            if (channelPackageBean.getId() == null) {
                return new ResponseEntity<>(new ApiResponse(channelPackageBean, HttpStatus.BAD_REQUEST, "error, el id es requerido,", true), HttpStatus.BAD_REQUEST);
            }

            Optional<ChannelPackageBean> foundPackage = channelPackageRepository.findById(channelPackageBean.getId());
            if (foundPackage.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Error: el paquete que intentas actualizar no existe", true), HttpStatus.NOT_FOUND);
            }

            ChannelPackageBean existing = foundPackage.get();

            if (existing.getStatus() == ChannelPackageStatus.OBSOLETO) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "Error: no se puede actualizar un paquete que ya no esta disponible", true), HttpStatus.CONFLICT);
            }

            if (existing.getStatus() == ChannelPackageStatus.DESCONTINUADO) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "Error: no se puede actualizar un paquete que ya fue descontinuado", true), HttpStatus.CONFLICT);
            }


            if (channelPackageBean.getName() == null || channelPackageBean.getName().isBlank()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: ingresa un nombre válido", true), HttpStatus.BAD_REQUEST);
            }

            Optional<ChannelPackageBean> duplicateNamePackage = channelPackageRepository.findChannelPackageBeanByNameAndStatus(channelPackageBean.getName(), ChannelPackageStatus.DISPONIBLE);
            if (duplicateNamePackage.isPresent() && !duplicateNamePackage.get().getId().equals(channelPackageBean.getId())) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: Ya existe un paquete de canales disponible con el nombre " + channelPackageBean.getName(), true), HttpStatus.BAD_REQUEST);
            }


            if (channelPackageBean.getDescription() == null || channelPackageBean.getDescription().isBlank()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: ingresa una descripción válida", true), HttpStatus.BAD_REQUEST);
            }


            if (channelPackageBean.getChannels() == null || channelPackageBean.getChannels().isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: el paquete debe contener al menos un canal", true), HttpStatus.BAD_REQUEST);
            }

            for (ChannelBean channelBean : channelPackageBean.getChannels()) {
                Optional<ChannelBean> foundChannel = channelRepository.findById(channelBean.getId());
                if (foundChannel.isEmpty()) {
                    return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Error: el canal con ID " + channelBean.getId() + " no existe", true), HttpStatus.NOT_FOUND);
                }
            }


            channelPackageBean.setUuid(foundPackage.get().getUuid());
            existing.setName(channelPackageBean.getName());
            existing.setDescription(channelPackageBean.getDescription());
            existing.setChannels(channelPackageBean.getChannels());
            existing.setStatus(ChannelPackageStatus.DISPONIBLE);

            ChannelPackageBean savedPackage = channelPackageRepository.save(existing);

            List<String> stringList = contractRepository.findDistinctEmailsByChannelPackage(channelPackageBean.getId());

            emailService.UpdatePackageEmail(stringList, savedPackage);

            return new ResponseEntity<>(new ApiResponse(savedPackage, HttpStatus.OK, "Paquete actaulizado correctamente", false), HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace(); // <-- imprime el error real en consola
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar el paquete de canales: " + e.getMessage(), true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByUuid(UUID uuid) {
        try {
            if (uuid == null) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "error, el uuid es requerido", true), HttpStatus.BAD_REQUEST);
            }
            Optional<ChannelPackageBean> foundPackage = channelPackageRepository.findChannelPackageBeanByUuid(uuid);

            if (foundPackage.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "error, el paquete que buscas no existe"), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new ApiResponse(foundPackage.get(), HttpStatus.OK, null, false), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("algo salio mal ", e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener el paquete de canales: " + e.getMessage(), true), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findAll() {
        try {
            List<ChannelPackageBean> disponibles = channelPackageRepository.findAllByStatus(ChannelPackageStatus.DISPONIBLE);
            return new ResponseEntity<>(new ApiResponse(disponibles, HttpStatus.OK, null, false), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener los paquetes de canales disponibles", true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> FindAllDescontinuado() {

        return new ResponseEntity<>(new ApiResponse(channelPackageRepository.findAllByStatus(ChannelPackageStatus.DESCONTINUADO), HttpStatus.OK, null, false), HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findAllObsoleto() {

        return new ResponseEntity<>(new ApiResponse(channelPackageRepository.findAllByStatus(ChannelPackageStatus.OBSOLETO), HttpStatus.OK, null, false), HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> setDescontinuado(Long id) {

        try {

            if (id == null) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: el id es requerido", true), HttpStatus.BAD_REQUEST);
            }

            Optional<ChannelPackageBean> foundChannelPackage = channelPackageRepository.findById(id);

            if (foundChannelPackage.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: no se encontró el paquete que intentas eliminar", true), HttpStatus.NOT_FOUND);
            }

            ChannelPackageBean channelPackageBean = foundChannelPackage.get();

            channelPackageBean.setStatus(ChannelPackageStatus.DESCONTINUADO);

            return new ResponseEntity<>(new ApiResponse(channelPackageRepository.save(channelPackageBean), HttpStatus.OK, "Se actualizó el paquete de canales a descontinuado", false), HttpStatus.OK);

        } catch (Exception e) {
            logger.error("algo salio mal ", e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar el paquete de canales: " + e.getMessage(), true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> setDisponile(Long id) {
        try {


            if (id == null) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: el id es requerido", true), HttpStatus.BAD_REQUEST);
            }

            Optional<ChannelPackageBean> foundChannelPackage = channelPackageRepository.findById(id);

            if (foundChannelPackage.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: no se encontró el paquete que intentas eliminar", true), HttpStatus.NOT_FOUND);
            }

            ChannelPackageBean channelPackageBean = foundChannelPackage.get();

            channelPackageBean.setStatus(ChannelPackageStatus.DISPONIBLE);

            return new ResponseEntity<>(new ApiResponse(channelPackageRepository.save(channelPackageBean), HttpStatus.OK, "Se actualizó el paquete de canales a disponible", false), HttpStatus.OK);

        } catch (Exception e) {
            logger.error("algo salio mal ", e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar el paquete de canales: " + e.getMessage(), true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> delete(Long id) {

        try {
            if (id == null) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: el id es requerido", true), HttpStatus.BAD_REQUEST);
            }

            Optional<ChannelPackageBean> foundChannelPackage = channelPackageRepository.findById(id);


            if (foundChannelPackage.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: no se encontró el paquete que intentas eliminar", true), HttpStatus.NOT_FOUND);
            }

            ChannelPackageBean channelPackageBean = foundChannelPackage.get();

            List<SalesPackageEntity> foundRelatedSalesPackage = salesPackageRepository.findByChannelPackage(channelPackageBean);

            if (!foundRelatedSalesPackage.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "Error: no se puede eliminar este paquete de canales porque esta asignado a un paquete de ventas", true), HttpStatus.CONFLICT);
            }

            channelPackageBean.setStatus(ChannelPackageStatus.OBSOLETO);

            return new ResponseEntity<>(new ApiResponse(channelPackageRepository.save(channelPackageBean), HttpStatus.OK, "Se elimino este paquete con éxito", false), HttpStatus.OK);

        } catch (Exception e) {
            logger.error("algo salio mal ", e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el paquete de canales: " + e.getMessage(), true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> countDisponible() {
        try {
            return new ResponseEntity<>(new ApiResponse(channelPackageRepository.countByStatus(ChannelPackageStatus.DISPONIBLE), HttpStatus.OK, null, false), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "ocurrió un error al obtener" +
                    "el conteo de los paquetes de venta disponbiles", false), HttpStatus.OK);
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> countDescontinuado() {
        try {
            return new ResponseEntity<>(new ApiResponse(channelPackageRepository.countByStatus(ChannelPackageStatus.DESCONTINUADO), HttpStatus.OK, null, false), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "ocurrió un error al obtener" +
                    "el conteo de los paquetes de venta descontinuados", false), HttpStatus.OK);
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> countObsoleto() {
        try {
            return new ResponseEntity<>(new ApiResponse(channelPackageRepository.countByStatus(ChannelPackageStatus.OBSOLETO), HttpStatus.OK, null, false), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "ocurrió un error al obtener" +
                    "el conteo de los paquetes de venta obsoletos", false), HttpStatus.OK);
        }
    }


}
