package mx.edu.utez.inteNarvaez.models.address;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Column(length = 36, unique = true, columnDefinition = "CHAR(36) NOT NULL")
    private String uuid;

    private Boolean status;


    @ManyToOne
    @JsonBackReference

    @JoinColumn(name = "client_id", nullable = false)
    private ClientBean client;

    @OneToMany(mappedBy = "address")
    @JsonIgnore
    private Set<ContractBean> contracts = new HashSet<>();

}