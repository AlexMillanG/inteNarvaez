package mx.edu.utez.inteNarvaez.models.address;

import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.client.ClientBean;

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
    
}
