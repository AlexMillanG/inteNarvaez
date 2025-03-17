package mx.edu.utez.inteNarvaez.models.role;

import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.user.UserEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "roles")
public class RoleBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private UUID uuid;

    @ManyToMany
    @JoinTable(
            name = "user_has_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserEntity> userEntities = new HashSet<>();
}
