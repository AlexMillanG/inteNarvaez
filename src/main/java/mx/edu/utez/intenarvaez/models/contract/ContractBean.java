package mx.edu.utez.intenarvaez.models.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.intenarvaez.models.address.AddressBean;
import mx.edu.utez.intenarvaez.models.salePackage.SalesPackageEntity;
import mx.edu.utez.intenarvaez.models.user.UserEntity;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "contracts")
public class ContractBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm")
    @Column(nullable = false)
    private Date creationDate;
    @Column(nullable = false)
    private boolean status;
    @Column(length = 36, unique = true)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private AddressBean address;

    @ManyToOne
    @JoinColumn(name = "sales_package_id", nullable = false)
    private SalesPackageEntity salesPackageEntity;


    @ManyToOne
    @JoinColumn(name = "sales_agent_id", nullable = false)
    private UserEntity salesAgent;



    public ContractBean() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }

    @PrePersist
    protected void onCreate() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }
}
