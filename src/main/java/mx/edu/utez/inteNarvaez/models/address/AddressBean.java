package mx.edu.utez.inteNarvaez.models.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.inteNarvaez.models.client.ClientBean;
import mx.edu.utez.inteNarvaez.models.contract.ContractBean;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Table(name = "addresses")
@Entity
@NoArgsConstructor
public class AddressBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 2556)
    private String street;

    @Column(nullable = false,length = 5)
    private String number;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 20)
    private String state;

    @Column(nullable = false,length = 5)
    private Integer zipCode;

    @Column(length = 36, unique = true, nullable = false)
    private UUID uuid;



    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnore
    private ClientBean client;

    @OneToMany(mappedBy = "address")
    @JsonIgnore
    private Set<ContractBean> contracts = new HashSet<>();

    public AddressBean(String name, String street, String number, String city, String state, Integer zipCode, Long uuid) {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }

    public AddressBean(String name, String street, String number, String city, String state, Integer zipCode, UUID uuid, ClientBean client) {
        this.name = name;
        this.street = street;
        this.number = number;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.uuid = uuid;
        this.client = client;
    }
}