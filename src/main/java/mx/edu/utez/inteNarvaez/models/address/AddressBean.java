package mx.edu.utez.inteNarvaez.models.address;

import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.client.ClientBean;
import mx.edu.utez.inteNarvaez.models.contract.ContractBean;

import java.util.HashSet;
import java.util.Set;

@Data
@Table(name = "addresses")
@Entity
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

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientBean client;

    @OneToMany(mappedBy = "address")
    private Set<ContractBean> contracts = new HashSet<>();
}