package mx.edu.utez.inteNarvaez.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore

    private String password;
    @JsonIgnore
    private Set<String> roles;

    public UserDTO(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.email = userEntity.getEmail();
                this.roles = userEntity.getRoleBeans().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());
    }
}
