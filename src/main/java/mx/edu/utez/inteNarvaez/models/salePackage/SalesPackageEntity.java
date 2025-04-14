package mx.edu.utez.inteNarvaez.models.salePackage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageBean;
import mx.edu.utez.inteNarvaez.models.contract.ContractBean;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "sales_packages")
public class SalesPackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Double totalAmount;
    @Column(nullable = false)
    private boolean status;
    @Column(length = 36, unique = true)
    private String uuid;

    private String speed;


    @ManyToOne
    @JoinColumn(name = "channel_package_id", nullable = false)
    private ChannelPackageBean channelPackage;


    @OneToMany(mappedBy = "salesPackageEntity")
    @JsonIgnore
    private Set<ContractBean> contracts = new HashSet<>();

    public SalesPackageEntity() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }

}
