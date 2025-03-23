package mx.edu.utez.inteNarvaez.models.contract;

import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.address.AddressBean;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageEntity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Data
@Entity
@Table(name = "contracts")
public class ContractBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate creationDate;
    private Double amount;
    @Column(length = 36, unique = true)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private AddressBean address;

    @ManyToOne
    @JoinColumn(name = "sales_package_id", nullable = false)
    private SalesPackageEntity salesPackageEntity;


    public ContractBean(LocalDate creationDate, Double amount, UUID uuid, AddressBean address, SalesPackageEntity salesPackageEntity) {

        this.creationDate = creationDate;
        this.amount = amount;
        this.uuid = uuid;
        this.address = address;
        this.salesPackageEntity = salesPackageEntity;
    }

    public ContractBean() {
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
