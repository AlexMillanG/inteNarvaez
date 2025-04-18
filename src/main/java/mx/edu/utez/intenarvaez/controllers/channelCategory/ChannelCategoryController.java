package mx.edu.utez.intenarvaez.controllers.channelCategory;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.utez.intenarvaez.config.ApiResponse;
import mx.edu.utez.intenarvaez.models.channelCategory.ChannelCategoryBean;
import mx.edu.utez.intenarvaez.services.channelCategory.ChannelCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/channelCategory")
@RequiredArgsConstructor
public class ChannelCategoryController {


    private final ChannelCategoryService channelCategoryService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveCategoryChannel(@Valid @RequestBody ChannelCategoryBean categoryBean , BindingResult result){
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}
        return channelCategoryService.saveCategoryChannel(categoryBean);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAllCategoryChannel(){
        return channelCategoryService.findAllCategoryChannel();
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateCategoryChannel(@Valid @RequestBody ChannelCategoryBean categoryBean , BindingResult result){
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}
        return channelCategoryService.updateCategoryChannel(categoryBean);
    }

    @GetMapping("findOne/{id}")
    public ResponseEntity<ApiResponse> findOneCategoryChannel(@PathVariable Long id){
        return channelCategoryService.findOneCategoryChannel(id);
    }

    @GetMapping("findByUuid/{uuid}")
    public ResponseEntity<ApiResponse> findByUuid(@PathVariable String uuid){
        return channelCategoryService.findByUuid(uuid);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id){
        return channelCategoryService.delete(id);
    }
}
