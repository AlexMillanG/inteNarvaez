package mx.edu.utez.intenarvaez.models.salePackage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utez.intenarvaez.models.channelPackage.ChannelPackageBean;
import mx.edu.utez.intenarvaez.models.contract.ContractBean;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "sales_packages")
@Getter
@Setter
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


    public SalesPackageEntity(Long id, String name, Double totalAmount, boolean status, String uuid, String speed, ChannelPackageBean channelPackage, Set<ContractBean> contracts) {
        this.id = id;
        this.name = name;
        this.totalAmount = totalAmount;
        this.status = status;
        this.uuid = uuid;
        this.speed = speed;
        this.channelPackage = channelPackage;
        this.contracts = contracts;
    }

    public SalesPackageEntity() {
    }
}
