package mx.edu.utez.intenarvaez.services.channel;

import lombok.AllArgsConstructor;
import mx.edu.utez.intenarvaez.config.ApiResponse;
import mx.edu.utez.intenarvaez.controllers.channel.dto.ChannelDTO;
import mx.edu.utez.intenarvaez.models.channel.ChannelBean;
import mx.edu.utez.intenarvaez.models.channel.ChannelRepository;
import mx.edu.utez.intenarvaez.models.channelCategory.ChannelCategoryBean;
import mx.edu.utez.intenarvaez.models.channelCategory.ChannelCategoryRepository;
import mx.edu.utez.intenarvaez.models.logo.LogoBean;
import mx.edu.utez.intenarvaez.models.logo.LogoRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static mx.edu.utez.intenarvaez.services.channelCategory.ChannelCategoryService.capitalize;

@Service
@Transactional(rollbackFor = SQLException.class)
@AllArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;

    private final ChannelCategoryRepository channelCategoryRepository;

    private final LogoRepository logoRepository;

    private static final Logger logger = LogManager.getLogger(ChannelService.class);


    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAllChannel() {
        return ResponseEntity.ok(new ApiResponse(channelRepository.findAll(), HttpStatus.OK, "Canales encontrados", false));
    }



    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByCategory(Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id de la categoría no puede ser nulo", true));
            }
            Optional<ChannelCategoryBean> foundCategory = channelCategoryRepository.findById(id);

            if (foundCategory.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoría no existe", true));
            }

            return ResponseEntity.ok(new ApiResponse(channelRepository.findByCategoryAndStatus(foundCategory.get(), true), HttpStatus.OK, "Canales encontrados", false));
        } catch (Exception e) {
            logger.error("Error de consulta: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al consultar ", true));
        }

    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByNumber(Integer number) {
        if (number == null || number <= 0) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El número del canal no puede ser nulo o menor a Cero", true));
        }
        Optional<ChannelBean> foundChannel = channelRepository.findByNumberAndStatus(number, true);
        try {

            if (foundChannel.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal no se encontro", true));
            }

            if (!foundChannel.get().getStatus()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal ha sido eliminado", true));

            }

            return ResponseEntity.ok(new ApiResponse(foundChannel.get(), HttpStatus.OK, "Canal encontrado", false));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "error ", true));
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByName(String name) {
        try {
            if (name == null || name.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El nombre del canal no puede ser nulo o vacío", true));
            }
            Optional<ChannelBean> foundChannel = channelRepository.findByName(name);

            if (foundChannel.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal no existe", true));
            }

            if (!foundChannel.get().getStatus()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal se elimino", true));
            }

            return ResponseEntity.ok(new ApiResponse(foundChannel.get(), HttpStatus.OK, "Se encontro", false));
        } catch (Exception e) {
            logger.error("Error al consultar ese canal: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error ", true));
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByUuid(String uuid) {

        try {
            if (uuid == null) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El uuid del canal no puede ser nulo", true));
            }
            Optional<ChannelBean> foundChannel = channelRepository.findByUuid(uuid);

            if (foundChannel.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal no esta disponible", true));
            }

            if (!foundChannel.get().getStatus()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Canal eliminado", true));

            }

            return ResponseEntity.ok(new ApiResponse(foundChannel.get(), HttpStatus.OK, "Canal allado", false));
        } catch (Exception e) {
            logger.error("Error al consultar el canal: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "error en la busqueda", true));
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> saveWithImage(ChannelDTO dto)  {

        try {

            ChannelBean channelBean = new ChannelBean();
            channelBean.setName(dto.getName());
            channelBean.setDescription(dto.getDescription());
            channelBean.setNumber(dto.getNumber());
            channelBean.setUuid(UUID.randomUUID().toString());

            ChannelCategoryBean channelCategoryBean = new ChannelCategoryBean();
            channelCategoryBean.setId(dto.getCategoryId());
            channelBean.setCategory(channelCategoryBean);


            Optional<ChannelCategoryBean> foundCategory = channelCategoryRepository.findById(channelBean.getCategory().getId());
            if (foundCategory.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoría no existe", true));
            }

            Optional<ChannelBean> foundNumber = channelRepository.findByNumberAndStatus(channelBean.getNumber(), true);
            if (foundNumber.isPresent()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya existe un canal con el numero ingresado " , true));
            }

            Optional<ChannelBean> foundName = channelRepository.findByName(capitalize(channelBean.getName()));
            if (foundName.isPresent()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya existe un canal con el nombre " + channelBean.getName(), true));
            }

            channelBean.setName(capitalize(channelBean.getName()));

            channelBean.setStatus(true);

            ChannelBean savedChannel = channelRepository.save(channelBean);

            if (dto.getImage() == null || dto.getImage().isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: archivo vacío o no válido", true));
            }

            LogoBean logo = new LogoBean();
            logo.setImage(dto.getImage().getBytes());
            logo.setChannel(savedChannel);

            String originalFilename = dto.getImage().getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                    : "";


            logo.setFileExtension(extension);
            logo.setFileExtension(extension);

            logoRepository.save(logo);
            return ResponseEntity.ok(new ApiResponse(savedChannel, HttpStatus.OK, "Canal guardado correctamente", false));

        } catch (Exception e) {
            logger.error("Error al guardar el canal: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al guardar el canal", true));
        }

    }

    public ResponseEntity<ApiResponse> updateWithImage(ChannelDTO dto) {
        try {
            if (dto.getId() == null || dto.getId() <= 0) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id del canal no puede ser nulo o menor a cero", true));
            }

            Optional<ChannelBean> foundChannel = channelRepository.findById(dto.getId());
            if (foundChannel.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal que intentas actualizar no existe", true));
            }

            Optional<ChannelCategoryBean> foundCategory = channelCategoryRepository.findById(dto.getCategoryId());
            if (foundCategory.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoría no existe", true));
            }

            Optional<ChannelBean> foundNumber = channelRepository.findByNumberAndStatus(dto.getNumber(), true);
            if (foundNumber.isPresent() && !foundNumber.get().getId().equals(dto.getId())) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya existe un canal con el número ingresado", true));
            }

            if ((dto.getImage() == null || dto.getImage().isEmpty()) && !Boolean.TRUE.equals(dto.getKeepImage())) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: archivo vacío o no válido", true));
            }

            ChannelBean channelBean = foundChannel.get();
            channelBean.setDescription(dto.getDescription());
            channelBean.setNumber(dto.getNumber());
            channelBean.setName(capitalize(dto.getName()));
            channelBean.setStatus(true);

            ChannelCategoryBean category = new ChannelCategoryBean();
            category.setId(dto.getCategoryId());
            channelBean.setCategory(category);
            ChannelBean savedChannel = channelRepository.save(channelBean);


            if (dto.getImage() != null && !dto.getImage().isEmpty()) {
                logoRepository.deleteById(savedChannel.getId());

                Optional<LogoBean> existingLogoOpt = logoRepository.findByChannel_Id(savedChannel.getId());

                LogoBean logo = existingLogoOpt.orElseGet(LogoBean::new);

                logo.setImage(dto.getImage().getBytes());
                logo.setChannel(savedChannel);

                String originalFilename = dto.getImage().getOriginalFilename();
                String extension = originalFilename != null && originalFilename.contains(".")
                        ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                        : "";
                logo.setFileExtension(extension);

                logoRepository.save(logo);
            }

            return ResponseEntity.ok(new ApiResponse(savedChannel, HttpStatus.OK, "Canal actualizado correctamente", false));

        } catch (Exception e) {
            logger.error("Error al actualizar el canal: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al actualizar el canal", true));
        }
    }

    public ResponseEntity<ApiResponse> delete(Long id, Long opc) {
        try {
            if (id == null || id <= 0) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id no puede ser nulo o menor a cero", true), HttpStatus.BAD_REQUEST);
            }
            Optional<ChannelBean> foundChannel = channelRepository.findById(id);

            if (foundChannel.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "No se encontró el canal  enviado", true), HttpStatus.NOT_FOUND);
            }

            ChannelBean channel = foundChannel.get();
            if (!channel.getChannelPackages().isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "Error, no se puede desactivar el canal porque está asociado a uno o más paquetes de canales", true), HttpStatus.CONFLICT);
            }



            if (opc == 1L) {

                if (!foundChannel.get().getStatus()) {
                    return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "el canal ya ha sido desactivado", true), HttpStatus.BAD_REQUEST);
                }

                channel.setStatus(false);
                channelRepository.save(channel);
                ChannelBean c = foundChannel.get();
                c.setStatus(false);
                channelRepository.saveAndFlush(c);

                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.OK, "el canal " + foundChannel.get().getName() + ", ha sido desactivado ", false), HttpStatus.OK);

            }

                channel.setStatus(true);
                channelRepository.save(channel);
                ChannelBean c = foundChannel.get();
                c.setStatus(true);
                channelRepository.saveAndFlush(c);
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.OK, "el canal " + foundChannel.get().getName() + ", ha sido Activado ", false), HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al desactivar el canal: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error con el canal", true));
        }
    }

    public ResponseEntity<ApiResponse> channelTotal() {
        try {
            return new ResponseEntity<>(new ApiResponse(channelRepository.countByStatus(true), HttpStatus.OK, null, false), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al eliminar el canal: ", e);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.OK, "hubo un error al hacer un conteo de canales disponibles", true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
