package mx.edu.utez.intenarvaez.controllers.channelPackage;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.intenarvaez.models.channel.ChannelBean;
import mx.edu.utez.intenarvaez.models.channelPackage.ChannelPackageBean;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
public class ChannelPackageDTO {

    private Long id;
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras, números y espacios.")
    private String name;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$", message = "La descripción solo puede contener letras, números y espacios.")
    private String description;

    @NotNull(message = "El conjunto de canales no puede ser nulo.")
    @Size(min = 1, message = "Debe haber al menos un canal asociado.")
    private Set<ChannelBean> channels = new HashSet<>();

    public ChannelPackageBean toEntity(){
        return new ChannelPackageBean(name, description, new ArrayList<>(channels));
    }

    public ChannelPackageBean toEntityUpdate(){
        return new ChannelPackageBean(id,name,description,new ArrayList<>(channels));
    }


}


