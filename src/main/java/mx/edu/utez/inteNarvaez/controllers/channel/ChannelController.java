package mx.edu.utez.inteNarvaez.controllers.channel;


import lombok.RequiredArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;
import mx.edu.utez.inteNarvaez.services.channel.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveChannel(@RequestBody ChannelBean channelBean){
        return channelService.saveChannel(channelBean);
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
}
