package mx.edu.utez.inteNarvaez.controllers.channel.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class ChannelDTO {
    //@NotBlank(message = "El nombre no puede estar vacío") // Asegura que no esté vacío
    //@Max
    private String name;

    private String description;

    private Integer number;

    private UUID uuid;

    private Long categoryId;

    @NotNull // Asegura que no sea nulo
    private MultipartFile image;



}
