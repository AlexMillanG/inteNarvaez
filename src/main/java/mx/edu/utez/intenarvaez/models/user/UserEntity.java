package mx.edu.utez.intenarvaez.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.intenarvaez.models.contract.ContractBean;
import mx.edu.utez.intenarvaez.models.role.RoleBean;

import java.util.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String firstName;
    private String lastName;
    private String surname;
    private String rfc;
    private String email;
    private String phone;
    @Temporal(TemporalType.DATE)
    private Date birthdate;
    private Date lastLogin;
    private Boolean status;
    private String password;
    private boolean temporalPassword;

    @ManyToMany(mappedBy = "userEntities")

    private Set<RoleBean> roleBeans = new HashSet<>();

    @OneToMany(mappedBy = "salesAgent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ContractBean> contracts = new ArrayList<>();

}