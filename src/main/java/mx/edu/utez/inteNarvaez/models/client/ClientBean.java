package mx.edu.utez.inteNarvaez.models.client;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utez.inteNarvaez.models.address.AddressBean;

import java.util.Date;
import java.util.List;

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

    @Column(unique = true)
    private String rfc;

    @Column( unique = true)
    private String email;

    @Column(length = 10)
    private String phone;

    @Temporal(TemporalType.DATE)
    private Date birthdate;

    private Boolean status;

    @Column(length = 36, unique = true, columnDefinition = "CHAR(36) NOT NULL")
    private String uuid;

    @OneToMany(mappedBy = "client")
    @JsonManagedReference
    private List<AddressBean> addresses;


    public ClientBean() {

    }

    public ClientBean(String name, String lastName, String surname, String rfc, String email, String phone, Date birthdate, Boolean status, String uuid) {
        this.name = name;
        this.lastName = lastName;
        this.surname = surname;
        this.rfc = rfc;
        this.email = email;
        this.phone = phone;
        this.birthdate = birthdate;
        this.status = status;
        this.uuid = uuid;
    }
    public ClientBean(Long id, String name, String lastName, String surname, String rfc, String email, String phone, Date birthdate, Boolean status, String uuid) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.surname = surname;
        this.rfc = rfc;
        this.email = email;
        this.phone = phone;
        this.birthdate = birthdate;
        this.status = status;
        this.uuid = uuid;
    }
}