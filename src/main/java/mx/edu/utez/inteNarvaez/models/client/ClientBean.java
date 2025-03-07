package mx.edu.utez.inteNarvaez.models.client;

import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.address.AddressBean;

import java.util.List;

@Data
@Table(name = "clients")
@Entity
public class ClientBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String lastname;

    @Column(length = 50)
    private String Surname;

    @Column(length = 13, unique = true)
    private String rfc;

    @Column(length = 100, unique = true)
    private String email;

    @OneToMany(mappedBy = "client", orphanRemoval = true)
    private List<AddressBean> addresses;
}