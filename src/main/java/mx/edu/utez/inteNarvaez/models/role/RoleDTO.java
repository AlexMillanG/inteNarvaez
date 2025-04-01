package mx.edu.utez.inteNarvaez.models.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private Long id;
    private String name;

     public RoleDTO(Long id, String name, UUID uuid) {

    }
}
