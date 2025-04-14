package mx.edu.utez.inteNarvaez.services.channel;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.controllers.channel.dto.ChannelDTO;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;
import mx.edu.utez.inteNarvaez.models.channel.ChannelRepository;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryBean;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryRepository;
import mx.edu.utez.inteNarvaez.models.logo.LogoBean;
import mx.edu.utez.inteNarvaez.models.logo.LogoRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static mx.edu.utez.inteNarvaez.services.channelCategory.ChannelCategoryService.capitalize;

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
        return ResponseEntity.ok(new ApiResponse(channelRepository.findByStatus(true), HttpStatus.OK, "Canales encontrados", false));
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> updateChannel(ChannelBean channelBean) {

        try {
            if (channelBean.getId() == null || channelBean.getId() <= 0) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id del canal no puede ser nulo o menor a cero", true));
            }

            Optional<ChannelBean> foundChannel = channelRepository.findById(channelBean.getId());

            if (foundChannel.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal que intentas actualizar no existe", true));
            }

            Optional<ChannelCategoryBean> foundCategory = channelCategoryRepository.findById(channelBean.getCategory().getId());

            if (foundCategory.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoría del canal no existe", true));
            }


            Optional<ChannelBean> foundNumber = channelRepository.findByNumberAndStatus(channelBean.getNumber(), true);

            if (foundNumber.isPresent()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya existe un canal con el número " + channelBean.getNumber(), true));
            }


            Optional<ChannelBean> foundName = channelRepository.findByName(capitalize(channelBean.getName()));
            if (foundName.isPresent()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya existe un canal con el nombre " + channelBean.getName(), true));
            }

            channelBean.setName(capitalize(channelBean.getName()));
            channelBean.setUuid(foundChannel.get().getUuid());
            channelBean.setStatus(foundChannel.get().getStatus());

            return ResponseEntity.ok(new ApiResponse(channelRepository.save(channelBean), HttpStatus.OK, "Canal guardado correctamente", false));

        } catch (Exception e) {
            logger.error("Error al actualizar el canal: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al actualizar el canal", true));
        }

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
            logger.error("Error al consultar el canal: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al consultar el canal", true));
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
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal no existe", true));
            }

            if (!foundChannel.get().getStatus()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal ha sido eliminado", true));

            }

            return ResponseEntity.ok(new ApiResponse(foundChannel.get(), HttpStatus.OK, "Canal encontrado", false));

        } catch (Exception e) {
            logger.error("Error al consultar el canal: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al consultar el canal", true));
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
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal ha sido eliminado", true));
            }

            return ResponseEntity.ok(new ApiResponse(foundChannel.get(), HttpStatus.OK, "Canal encontrado", false));
        } catch (Exception e) {
            logger.error("Error al consultar el canal: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al consultar el canal", true));
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
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal no existe", true));
            }

            if (!foundChannel.get().getStatus()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal ha sido eliminado", true));

            }

            return ResponseEntity.ok(new ApiResponse(foundChannel.get(), HttpStatus.OK, "Canal encontrado", false));
        } catch (Exception e) {
            logger.error("Error al consultar el canal: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al consultar el canal", true));
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> saveWithImage(ChannelDTO dto) throws IOException {

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
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoría del canal no existe", true));
            }

            Optional<ChannelBean> foundNumber = channelRepository.findByNumberAndStatus(channelBean.getNumber(), true);
            if (foundNumber.isPresent()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya existe un canal con el número " + channelBean.getNumber(), true));
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

    public ResponseEntity<ApiResponse> updateWithImage(ChannelDTO dto) throws IOException {
        try {
            if (dto.getId() == null || dto.getId() <= 0) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id del canal no puede ser nulo o menor a cero", true));
            }
            Optional<ChannelBean> foundChannelOpt = channelRepository.findById(dto.getId());
            if (foundChannelOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, HttpStatus.NOT_FOUND, "El canal no existe", true));
            }

            ChannelBean channelBean = foundChannelOpt.get();

            if (dto.getName() == null || dto.getName().isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El nombre del canal no puede ser nulo o vacío", true));
            }
            if (dto.getDescription() == null || dto.getDescription().isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La descripción del canal no puede ser nula o vacía", true));
            }
            if (dto.getNumber() == null) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El número del canal no puede ser nulo", true));
            }
            if (dto.getCategoryId() == null) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoría del canal no puede ser nula", true));
            }

            Optional<ChannelCategoryBean> foundCategory = channelCategoryRepository.findById(dto.getCategoryId());
            if (foundCategory.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoría del canal no existe", true));
            }

            Optional<ChannelBean> foundNumber = channelRepository.findByNumberAndStatus(dto.getNumber(), true);
            if (foundNumber.isPresent() && !foundNumber.get().getId().equals(channelBean.getId())) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya existe un canal con el número " + dto.getNumber(), true));
            }

            Optional<ChannelBean> foundName = channelRepository.findByName(capitalize(dto.getName()));
            if (foundName.isPresent() && !foundName.get().getId().equals(channelBean.getId())) {
                return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya existe un canal con el nombre " + dto.getName(), true));
            }

            channelBean.setName(capitalize(dto.getName()));
            channelBean.setDescription(dto.getDescription());
            channelBean.setNumber(dto.getNumber());
            channelBean.setCategory(foundCategory.get());
            channelBean.setStatus(true);

            ChannelBean updatedChannel = channelRepository.save(channelBean);

            if (dto.getImage() == null) {
                System.err.println("no llegó la imagen");
            }

            if (dto.getImage() != null && !dto.getImage().isEmpty()) {
                Optional<LogoBean> foundLogoOpt = logoRepository.findByChannel_Id(updatedChannel.getId());

                LogoBean logo = foundLogoOpt.orElseGet(LogoBean::new);
                logo.setChannel(updatedChannel);
                logo.setImage(dto.getImage().getBytes());

                String originalFilename = dto.getImage().getOriginalFilename();
                String extension = originalFilename != null && originalFilename.contains(".")
                        ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                        : "";

                logo.setFileExtension(extension);
                logoRepository.save(logo);
            }

            return ResponseEntity.ok(new ApiResponse(updatedChannel, HttpStatus.OK, "Canal actualizado correctamente", false));
        } catch (Exception e) {
            logger.error("Error al guardar el canal: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al guardar el canal", true));
        }
    }

    public ResponseEntity<ApiResponse> delete(Long id) {
        try {
            if (id == null || id <= 0) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id no puede ser nulo o menor a cero", true), HttpStatus.BAD_REQUEST);
            }
            Optional<ChannelBean> foundChannel = channelRepository.findById(id);

            if (foundChannel.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "no se encontró el canal que intentas eliminar", true), HttpStatus.NOT_FOUND);
            }
            ChannelBean channel = foundChannel.get();

            if (!channel.getChannelPackages().isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.CONFLICT, "error, no se puede eliminar el canal, porque esta asociado a uno  o más paquetes de canales", true), HttpStatus.CONFLICT);
            }
            channel.setStatus(false);
            channelRepository.save(channel);
            if (!foundChannel.get().getStatus()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "el canal ya ha sido eliminado", true), HttpStatus.BAD_REQUEST);
            }
            ChannelBean c = foundChannel.get();
            c.setStatus(false);
            channelRepository.saveAndFlush(c);
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.OK, "el canal " + foundChannel.get().getName() + ", ha sido eliminado con éxito", false), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al eliminar el canal: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al eliminar el canal", true));
        }
    }

    public ResponseEntity<ApiResponse> channelTotal() {
        try {
            return new ResponseEntity<>(new ApiResponse(channelRepository.countByStatus(true), HttpStatus.OK, null, false), HttpStatus.OK);
        } catch (Error e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.OK, "hubo un error al hacer un conteo de canales disponibles", true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
