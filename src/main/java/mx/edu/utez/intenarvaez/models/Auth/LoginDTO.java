package mx.edu.utez.intenarvaez.models.Auth;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginDTO {
    private String email;
    private  String password;

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public LoginDTO() {
    }
}
