package mx.edu.utez.inteNarvaez.models.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private Long id;
    private String name;

     public RoleDTO(Long id, String name, String uuid) {

    }
}
