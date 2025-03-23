package mx.edu.utez.inteNarvaez.models.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
    private String name;
    private String street;
    private Integer number;
    private String city;
    private String state;
    private Integer zipCode;
    @Column(length = 36, unique = true)
    private UUID uuid;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientBean client;

    @OneToMany(mappedBy = "address")
    private Set<ContractBean> contracts = new HashSet<>();

    public AddressBean(String name, String street, Integer number, String city, String state, Integer zipCode, Long uuid) {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }


}