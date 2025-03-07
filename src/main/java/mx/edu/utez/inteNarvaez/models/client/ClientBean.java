package mx.edu.utez.inteNarvaez.models.client;

import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.address.AddressBean;

import java.util.List;
import java.util.UUID;

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


    private UUID uuid;

    @OneToMany(mappedBy = "client")
    private List<AddressBean> addresses;



    public ClientBean() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }

    @PrePersist
    protected void onCreate() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }
}