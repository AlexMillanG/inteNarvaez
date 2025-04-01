package mx.edu.utez.inteNarvaez.controllers.channel;


import lombok.RequiredArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.controllers.channel.dto.ChannelDTO;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;
import mx.edu.utez.inteNarvaez.services.channel.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveChannel(@RequestBody ChannelBean channelBean){
        /*

        {
            "name":"    Cartoon Network  ",
            "description":"Canal de Warner Bros enfocado a animación infantil",
            "number":24,
            "image":"https://upload.wikimedia.org/wikipedia/commons/8/80/Cartoon_Network_2010_logo.svg",
            "category":{
                "id":1
            }
        }

         */
        return channelService.saveChannel(channelBean);
    }

    @PostMapping("/saveImg")
    public ResponseEntity<ApiResponse> saveChannelImage(@ModelAttribute ChannelDTO dto) throws IOException {

        if(dto.getImage().isEmpty()){
            System.err.println("imagen nula");
        }
        return channelService.saveWithImage(dto);
    }


    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAllChannel(){
        return channelService.findAllChannel();
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateChannel(@RequestBody ChannelBean channelBean){
        return channelService.updateChannel(channelBean);
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<ApiResponse> findOneChannel(@PathVariable UUID uuid){
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
    public ResponseEntity<ApiResponse> findByUuid(@PathVariable UUID uuid){
        return channelService.findByUuid(uuid);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id){
        return channelService.delete(id);
    }
}
