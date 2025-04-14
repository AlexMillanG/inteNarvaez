package mx.edu.utez.inteNarvaez.models.role;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.user.UserEntity;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "roles")
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class RoleBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
    @Column(length = 10, unique = true)
    private String name;
    @Column(length = 36, unique = true)
    private String uuid;

    @ManyToMany()
    @JoinTable(
            name = "user_has_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<UserEntity> userEntities = new HashSet<>();

    public RoleBean( String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }
    public RoleBean() {
    }

}
