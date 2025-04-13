package mx.edu.utez.inteNarvaez.controllers.channelPackage;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.services.channelPackage.ChannelPackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/api/channelPackage")
@RequiredArgsConstructor
public class ChannelPackageController {

    private final ChannelPackageService channelPackageService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAll(){
        return channelPackageService.findAll();
    }

    @GetMapping("/findByUuid/{uuid}")
    public ResponseEntity<ApiResponse> findByUuid(@PathVariable UUID uuid){
        return channelPackageService.findByUuid(uuid);
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> save(@Valid @RequestBody ChannelPackageDTO dto, BindingResult result) {

        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}

        return channelPackageService.save(dto.toEntity());
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> update(@RequestBody ChannelPackageDTO dto) {


        return channelPackageService.update(dto.toEntityUpdate());
    }

    @GetMapping("/findDiscontinued")
    public ResponseEntity<ApiResponse> findDiscontinued(){
        return channelPackageService.FindAllDescontinuado();
    }

    @GetMapping("/findObsolete")
    public ResponseEntity<ApiResponse> findObsolete(){
        return channelPackageService.findAllObsoleto();
    }

    @PutMapping("/setDiscontinued/{id}")
    public ResponseEntity<ApiResponse> setDiscontinued(@PathVariable Long id){
        return channelPackageService.setDescontinuado(id);
    }

    @PutMapping("/setAvailable/{id}")
    public ResponseEntity<ApiResponse> setAvailable(@PathVariable Long id){
        return channelPackageService.setDisponile(id);
    }

    //lo setea como obsoleto
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id){
        return channelPackageService.delete(id);
    }


    @GetMapping("/AvailableCount/")
    public ResponseEntity<ApiResponse> countAvailable(){
        return channelPackageService.countDisponible();
    }

    @GetMapping("/ObsoleteCount/")
    public ResponseEntity<ApiResponse> countObsolete(){
        return channelPackageService.countObsoleto();
    }

    @GetMapping("/DiscontinuedCount/")
    public ResponseEntity<ApiResponse> countDiscontinued(){
        return channelPackageService.countDescontinuado();
    }
}
