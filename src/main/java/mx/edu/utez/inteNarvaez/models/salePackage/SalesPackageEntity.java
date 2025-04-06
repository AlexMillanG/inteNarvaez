package mx.edu.utez.inteNarvaez.models.salePackage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageBean;
import mx.edu.utez.inteNarvaez.models.contract.ContractBean;
import mx.edu.utez.inteNarvaez.models.products.ProductBean;

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
    private String name;
    private Double totalAmount;
    @Column(length = 36, unique = true)
    private UUID uuid;

    private String speed;


    @ManyToOne
    @JoinColumn(name = "channel_package_id", nullable = false)
    private ChannelPackageBean channelPackage;






    @OneToMany(mappedBy = "salesPackageEntity")
    @JsonIgnore
    private Set<ContractBean> contracts = new HashSet<>();

    public SalesPackageEntity() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }

    public SalesPackageEntity(String name, Double totalAmount, UUID uuid, ChannelPackageBean channelPackage, ProductBean productBean,String speed) {
        this.name = name;
        this.totalAmount = totalAmount;
        this.uuid = uuid;
        this.channelPackage = channelPackage;
        this.speed = speed;
    }
}
