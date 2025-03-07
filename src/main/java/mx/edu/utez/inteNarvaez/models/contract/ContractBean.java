package mx.edu.utez.inteNarvaez.models.contract;

import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.address.AddressBean;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageBean;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "contracts")
public class ContractBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate creationDate;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private AddressBean address;


    @ManyToOne
    @JoinColumn(name = "sales_package_id", nullable = false)
    private SalesPackageBean salesPackageBean;
}
