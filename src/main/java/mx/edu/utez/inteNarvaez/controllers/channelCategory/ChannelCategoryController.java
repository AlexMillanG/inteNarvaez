package mx.edu.utez.inteNarvaez.controllers.channelCategory;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryBean;
import mx.edu.utez.inteNarvaez.services.channelCategory.ChannelCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/channelCategory")
@RequiredArgsConstructor
public class ChannelCategoryController {


    private final ChannelCategoryService channelCategoryService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveCategoryChannel(@RequestBody ChannelCategoryBean categoryBean){

        return channelCategoryService.saveCategoryChannel(categoryBean);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAllCategoryChannel(){
        return channelCategoryService.findAllCategoryChannel();
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateCategoryChannel(@RequestBody ChannelCategoryBean categoryBean){
        return channelCategoryService.updateCategoryChannel(categoryBean);
    }

    @GetMapping("findOne/{id}")
    public ResponseEntity<ApiResponse> findOneCategoryChannel(@PathVariable Long id){
        return channelCategoryService.findOneCategoryChannel(id);
    }

    @GetMapping("findByUuid/{uuid}")
    public ResponseEntity<ApiResponse> findByUuid(@PathVariable UUID uuid){
        return channelCategoryService.findByUuid(uuid);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id){
        return channelCategoryService.delete(id);
    }
}
