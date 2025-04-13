package mx.edu.utez.inteNarvaez.models.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class LoginDTO {
    private String email;
    private  String password;
}
