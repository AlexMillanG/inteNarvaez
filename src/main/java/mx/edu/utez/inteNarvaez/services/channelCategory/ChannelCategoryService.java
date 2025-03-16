package mx.edu.utez.inteNarvaez.services.channelCategory;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryBean;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = SQLException.class)
@AllArgsConstructor
public class ChannelCategoryService {

    private final ChannelCategoryRepository channelCategoryRepository;


    public ResponseEntity<ApiResponse> saveCategoryChannel(ChannelCategoryBean categoryBean){

        if (categoryBean.getName().equals("") || categoryBean.getName() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El nombre de la categoria no puede ser nulo", true));
        }

        Optional<ChannelCategoryBean> foundChannelCategory = channelCategoryRepository.findByName(capitalize(categoryBean.getName()));

        if(foundChannelCategory.isPresent())
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoria ya existe", true));

        categoryBean.setName(capitalize(categoryBean.getName().trim()));

        return ResponseEntity.ok(new ApiResponse(channelCategoryRepository.save(categoryBean), HttpStatus.OK, "Categoria creada correctamente", false));
    }

    public ResponseEntity<ApiResponse> findAllCategoryChannel(){
        return ResponseEntity.ok(new ApiResponse(channelCategoryRepository.findAll(), HttpStatus.OK, null, false));
    }

    public ResponseEntity<ApiResponse> updateCategoryChannel(ChannelCategoryBean categoryBean){
        Optional<ChannelCategoryBean> foundCategory = channelCategoryRepository.findById(categoryBean.getId());

        if (foundCategory.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoria no existe", true));
        }


        if (categoryBean.getName().equals("") || categoryBean.getName() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El nombre de la categoria no puede ser nulo", true));
        }

        categoryBean.setUuid(foundCategory.get().getUuid());
        return ResponseEntity.ok(new ApiResponse(channelCategoryRepository.save(categoryBean), HttpStatus.OK, "Categoria actualizada correctamente", false));
    }

    public ResponseEntity<ApiResponse> findOneCategoryChannel(Long id){
        Optional<ChannelCategoryBean> foundCategory = channelCategoryRepository.findById(id);

        if (foundCategory.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoria no existe", true));
        }

        return ResponseEntity.ok(new ApiResponse(foundCategory.get(), HttpStatus.OK, null, false));
    }

    public ResponseEntity<ApiResponse> findByUuid(UUID uuid){
        Optional<ChannelCategoryBean> foundCategory = channelCategoryRepository.findByUuid(uuid);

        if (foundCategory.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La categoria no existe", true));
        }

        return ResponseEntity.ok(new ApiResponse(foundCategory.get(), HttpStatus.OK, null, false));
    }



    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase().trim();
    }
}
