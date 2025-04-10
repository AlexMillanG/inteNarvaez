package mx.edu.utez.inteNarvaez.services.channel;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.controllers.channel.dto.ChannelDTO;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;
import mx.edu.utez.inteNarvaez.models.channel.ChannelRepository;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryBean;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryRepository;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageRepository;
import mx.edu.utez.inteNarvaez.models.logo.LogoBean;
import mx.edu.utez.inteNarvaez.models.logo.LogoRepository;
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

    private final ChannelPackageRepository channelPackageRepository;

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> saveChannel(ChannelBean channelBean){

        if (channelBean.getName() == null || channelBean.getName().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El nombre del canal no puede ser nulo o vacío", true));
        }

        if (channelBean.getDescription() == null || channelBean.getDescription().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La descripción del canal no puede ser nula o vacía", true));
        }

        if (channelBean.getNumber() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El número del canal no puede ser nulo", true));
        }

        if (channelBean.getCategory() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoría del canal no puede ser nula", true));
        }

        Optional<ChannelCategoryBean> foundCategory = channelCategoryRepository.findById(channelBean.getCategory().getId());

        if (foundCategory.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoría del canal no existe", true));
        }



        Optional<ChannelBean> foundNumber = channelRepository.findByNumberAndStatus(channelBean.getNumber(),true);

        if (foundNumber.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya existe un canal con el número " +channelBean.getNumber(), true));
        }


        Optional<ChannelBean> foundName = channelRepository.findByName(capitalize(channelBean.getName()));
        if (foundName.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya existe un canal con el nombre " +channelBean.getName(), true));
        }

        channelBean.setName(capitalize(channelBean.getName()));
        channelBean.setStatus(true);



        return ResponseEntity.ok(new ApiResponse(channelRepository.save(channelBean), HttpStatus.OK, "Canal guardado correctamente", false));

    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAllChannel(){
        return ResponseEntity.ok(new ApiResponse(channelRepository.findByStatus(true), HttpStatus.OK, "Canales encontrados", false));
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> updateChannel(ChannelBean channelBean){

        if (channelBean.getId() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El id del canal no puede ser nulo", true));
        }

        Optional<ChannelBean> foundChannel = channelRepository.findById(channelBean.getId());

        if (foundChannel.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal que intentas actualizar no existe", true));
        }


        if (channelBean.getName() == null || channelBean.getName().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El nombre del canal no puede ser nulo o vacío", true));
        }

        if (channelBean.getDescription() == null || channelBean.getDescription().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La descripción del canal no puede ser nula o vacía", true));
        }

        if (channelBean.getNumber() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El número del canal no puede ser nulo", true));
        }

        if (channelBean.getCategory() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoría del canal no puede ser nula", true));
        }


        Optional<ChannelCategoryBean> foundCategory = channelCategoryRepository.findById(channelBean.getCategory().getId());

        if (foundCategory.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoría del canal no existe", true));
        }


        Optional<ChannelBean> foundNumber = channelRepository.findByNumberAndStatus(channelBean.getNumber(),true);

        if (foundNumber.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya existe un canal con el número " +channelBean.getNumber(), true));
        }


        Optional<ChannelBean> foundName = channelRepository.findByName(capitalize(channelBean.getName()));
        if (foundName.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Ya existe un canal con el nombre " +channelBean.getName(), true));
        }

        channelBean.setName(capitalize(channelBean.getName()));

        channelBean.setUuid(foundChannel.get().getUuid());

        channelBean.setStatus(foundChannel.get().getStatus());


        return ResponseEntity.ok(new ApiResponse(channelRepository.save(channelBean), HttpStatus.OK, "Canal guardado correctamente", false));

    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByCategory(Long id){
        Optional<ChannelCategoryBean> foundCategory = channelCategoryRepository.findById(id);

        if (foundCategory.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoría no existe", true));
        }

        return ResponseEntity.ok(new ApiResponse(channelRepository.findByCategoryAndStatus(foundCategory.get(),true), HttpStatus.OK, "Canales encontrados", false));
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByNumber(Integer number){
        Optional<ChannelBean> foundChannel = channelRepository.findByNumberAndStatus(number,true);

        if (foundChannel.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal no existe", true));
        }

        if (!foundChannel.get().getStatus()){
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal ha sido eliminado", true));

        }

        return ResponseEntity.ok(new ApiResponse(foundChannel.get(), HttpStatus.OK, "Canal encontrado", false));
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByName(String name){
        Optional<ChannelBean> foundChannel = channelRepository.findByName(name);

        if (foundChannel.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal no existe", true));
        }

         if (!foundChannel.get().getStatus()) {
             return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal ha sido eliminado", true));
         }

        return ResponseEntity.ok(new ApiResponse(foundChannel.get(), HttpStatus.OK, "Canal encontrado", false));
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> findByUuid(UUID uuid){
        Optional<ChannelBean> foundChannel = channelRepository.findByUuid(uuid);

        if (foundChannel.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal no existe", true));
        }

        if (!foundChannel.get().getStatus()){
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El canal ha sido eliminado", true));

        }

        return ResponseEntity.ok(new ApiResponse(foundChannel.get(), HttpStatus.OK, "Canal encontrado", false));
    }
    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> saveWithImage(ChannelDTO dto) throws IOException {
        ChannelBean channelBean = new ChannelBean();
        channelBean.setName(dto.getName());
        channelBean.setDescription(dto.getDescription());
        channelBean.setNumber(dto.getNumber());
        channelBean.setUuid(UUID.randomUUID());

        ChannelCategoryBean channelCategoryBean = new ChannelCategoryBean();
        channelCategoryBean.setId(dto.getCategoryId());
        channelBean.setCategory(channelCategoryBean);

        // Validaciones
        if (channelBean.getName() == null || channelBean.getName().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El nombre del canal no puede ser nulo o vacío", true));
        }
        if (channelBean.getDescription() == null || channelBean.getDescription().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La descripción del canal no puede ser nula o vacía", true));
        }
        if (channelBean.getNumber() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El número del canal no puede ser nulo", true));
        }
        if (channelBean.getCategory() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoría del canal no puede ser nula", true));
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

        channelBean.setStatus(true);

        // Guardar el canal primero
        ChannelBean savedChannel = channelRepository.save(channelBean);

        // Validación de imagen
        if (dto.getImage() == null || dto.getImage().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: archivo vacío o no válido", true));
        }

        // Guardar imagen después de guardar el canal
        LogoBean logo = new LogoBean();
        logo.setImage(dto.getImage().getBytes()); // Convierte el archivo en bytes
        logo.setChannel(savedChannel);

        String originalFilename = dto.getImage().getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                : "";



        logo.setFileExtension(extension);
        logo.setFileExtension(extension);

        logoRepository.save(logo);

        return ResponseEntity.ok(new ApiResponse(savedChannel, HttpStatus.OK, "Canal guardado correctamente", false));


    }

    public ResponseEntity<ApiResponse> delete(Long id){
        Optional<ChannelBean> foundChannel = channelRepository.findById(id);

        if (foundChannel.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.NOT_FOUND,"no se encontró el canal que intentas eliminar",true),HttpStatus.NOT_FOUND);
        }


        ChannelBean channel = foundChannel.get();


        if (!channel.getChannelPackages().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.CONFLICT,"error, no se puede eliminar el canal, porque esta asociado a uno  o más paquetes de canales",true),HttpStatus.CONFLICT);
        }


        channel.setStatus(false);
        channelRepository.save(channel);


        if (!foundChannel.get().getStatus()){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.BAD_REQUEST,"el canal ya ha sido eliminado",true),HttpStatus.BAD_REQUEST);
        }

        ChannelBean c = foundChannel.get();
        c.setStatus(false);
        channelRepository.saveAndFlush(c);
        return new ResponseEntity<>(new ApiResponse(null,HttpStatus.OK,"el canal " + foundChannel.get().getName() +", ha sido eliminado con éxito",false),HttpStatus.OK);
    }


}
