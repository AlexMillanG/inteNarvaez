package mx.edu.utez.inteNarvaez.models.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utez.inteNarvaez.models.role.RoleBean;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String firstName;
    private  String lastName;
    private  String email;
    private String password;
    private boolean temporalPassword;

    @ManyToMany(mappedBy = "userEntities")
    private Set<RoleBean> roleBeans = new HashSet<>();

}