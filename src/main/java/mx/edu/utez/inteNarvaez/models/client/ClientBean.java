package mx.edu.utez.inteNarvaez.models.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utez.inteNarvaez.models.address.AddressBean;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Table(name = "clients")
@Entity
@Getter
@Setter
@AllArgsConstructor
public class ClientBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String lastName;

    @Column(length = 50)
    private String surname;

    @Column(length = 13, unique = true)
    private String rfc;

    @Column(length = 100, unique = true)
    private String email;

    @Column(length = 10)
    private String phone;

    @Temporal(TemporalType.DATE)
    private Date birthdate;

    @Column(length = 36, unique = true)
    private UUID uuid;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<AddressBean> addresses;
    public ClientBean() {

    }


}