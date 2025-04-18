package mx.edu.utez.intenarvaez.controllers.channel;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.utez.intenarvaez.config.ApiResponse;
import mx.edu.utez.intenarvaez.controllers.channel.dto.ChannelDTO;
import mx.edu.utez.intenarvaez.models.channel.ChannelBean;
import mx.edu.utez.intenarvaez.services.channel.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;


    @PostMapping("/saveImg")
    public ResponseEntity<ApiResponse> saveChannelImage(@Valid @ModelAttribute ChannelDTO dto,BindingResult result)  {
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}
        return channelService.saveWithImage(dto);
    }

    @PutMapping("/updateImg")
    public ResponseEntity<ApiResponse> update( @Valid @ModelAttribute ChannelDTO dto ,BindingResult result)  {
        if (ApiResponse.hasValidationErrors(result)) {return ApiResponse.buildErrorResponse(result);}
        return channelService.updateWithImage(dto);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAllChannel(){
        return channelService.findAllChannel();
    }


    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<ApiResponse> findOneChannel(@PathVariable String uuid){
        return channelService.findByUuid(uuid);
    }

    @GetMapping("/number/{number}")
    public ResponseEntity<ApiResponse> findByNumber(@PathVariable Integer number){
        return channelService.findByNumber(number);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse> findByCategory(@PathVariable Long category){
        return channelService.findByCategory(category);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> findByName(@PathVariable String name){
        return channelService.findByName(name);
    }

    @GetMapping("/findByUuid/{uuid}")
    public ResponseEntity<ApiResponse> findByUuid(@PathVariable String uuid){
        return channelService.findByUuid(uuid);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id){
        return channelService.delete(id , 1L);
    }

    @GetMapping("/active/{id}")
    public ResponseEntity<ApiResponse> active(@PathVariable Long id){
        return channelService.delete(id, 2L);
    }

    @GetMapping("/channelCount/")
    public ResponseEntity<ApiResponse> channelCount(){
        return channelService.channelTotal();
    }
}
