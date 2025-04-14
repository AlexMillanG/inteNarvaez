package mx.edu.utez.inteNarvaez.services.channelCategory;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;
import mx.edu.utez.inteNarvaez.models.channel.ChannelRepository;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryBean;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = SQLException.class)
@AllArgsConstructor
public class ChannelCategoryService {

    private final ChannelCategoryRepository channelCategoryRepository;
    private final ChannelRepository channelRepository;
    private static final Logger logger = LogManager.getLogger(ChannelCategoryService.class);

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> saveCategoryChannel(ChannelCategoryBean categoryBean) {
        try {

            if (categoryBean.getName().equals("") || categoryBean.getName() == null) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El nombre de la categoria no puede ser nulo", true));
            }

            Optional<ChannelCategoryBean> foundChannelCategory = channelCategoryRepository.findByName(capitalize(categoryBean.getName()));

            if (foundChannelCategory.isPresent())
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoria ya existe", true));

            categoryBean.setName(capitalize(categoryBean.getName().trim()));
            categoryBean.setStatus(true);

            return ResponseEntity.ok(new ApiResponse(channelCategoryRepository.save(categoryBean), HttpStatus.OK, "Categoria creada correctamente", false));
        } catch (Exception e) {
            logger.error("Error al guardar la categoria: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al guardar la categoria", true));
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findAllCategoryChannel() {
        try {
            return ResponseEntity.ok(new ApiResponse(channelCategoryRepository.findByStatus(true), HttpStatus.OK, null, false));

        } catch (Exception e) {
            logger.error("Error al consultar los datos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al solicitar la información", true));
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> updateCategoryChannel(ChannelCategoryBean categoryBean) {
        try {
            if (categoryBean.getId() == null || categoryBean.getId() <= 0) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id no puede ser nulo", true));
            }

            Optional<ChannelCategoryBean> foundCategory = channelCategoryRepository.findById(categoryBean.getId());


            if (foundCategory.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoria no existe", true));
            }

            if (!foundCategory.get().getStatus()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "ERROR, no puede actulizar una categoria eliminada", true), HttpStatus.CONFLICT);
            }

            if (categoryBean.getName().equals("") || categoryBean.getName() == null) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El nombre de la categoria no puede ser nulo", true));
            }

            categoryBean.setStatus(foundCategory.get().getStatus());

            categoryBean.setUuid(foundCategory.get().getUuid());
            return ResponseEntity.ok(new ApiResponse(channelCategoryRepository.save(categoryBean), HttpStatus.OK, "Categoria actualizada correctamente", false));
        } catch (Exception e) {
            logger.error("Error al consultar los datos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al solicitar la información", true));
        }

    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findOneCategoryChannel(Long id) {

        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id no puede ser nulo", true));
            }
            Optional<ChannelCategoryBean> foundCategory = channelCategoryRepository.findById(id);

            if (foundCategory.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoria no existe", true));
            }

            if (!foundCategory.get().getStatus()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoria se ha eliminado", true));

            }

            return ResponseEntity.ok(new ApiResponse(foundCategory.get(), HttpStatus.OK, null, false));
        } catch (Exception e) {
            logger.error("Error al consultar los datos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al solicitar la información", true));
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByUuid(String uuid) {
        try {
            if (uuid == null) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El uuid no puede ser nulo", true));
            }
            Optional<ChannelCategoryBean> foundCategory = channelCategoryRepository.findByUuid(uuid);

            if (foundCategory.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoria no existe", true));
            }

            if (!foundCategory.get().getStatus()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoria esta eliminada", true));
            }

            return ResponseEntity.ok(new ApiResponse(foundCategory.get(), HttpStatus.OK, null, false));
        } catch (Exception e) {
            logger.error("Error al consultar los datos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al solicitar la información", true));
        }

    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> delete(Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id no puede ser nulo", true));
            }
            Optional<ChannelCategoryBean> foundChannelCategory = channelCategoryRepository.findById(id);

            if (foundChannelCategory.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoria no existe", true));
            }

            if (!foundChannelCategory.get().getStatus()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoria ya ha sido eliminada", true));
            }

            List<ChannelBean> foundChannels = channelRepository.findByCategoryAndStatus(foundChannelCategory.get(), true);

            if (!foundChannels.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "ERROR, no se puede eliminar esta catagoria, tiene canales activos relacionados", true), HttpStatus.CONFLICT);
            }

            ChannelCategoryBean c = foundChannelCategory.get();

            c.setStatus(false);

            channelCategoryRepository.saveAndFlush(c);

            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.OK, "la categoría " + c.getName() + ", ha sido eliminada con éxito", false), HttpStatus.OK);


        } catch (Exception e) {
            logger.error("Error al consultar los datos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al solicitar la información", true));
        }


    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase().trim();
    }
}
