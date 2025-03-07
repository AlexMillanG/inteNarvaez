package mx.edu.utez.inteNarvaez.models.salePackage;

import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageBean;
import mx.edu.utez.inteNarvaez.models.contract.ContractBean;
import mx.edu.utez.inteNarvaez.models.products.ProductBean;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "sales_packages")
public class SalesPackageBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double totalAmount;


    @ManyToOne
    @JoinColumn(name = "channel_package_id", nullable = false)
    private ChannelPackageBean channelPackage;

    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private ProductBean productBean;


    @OneToMany(mappedBy = "salesPackageBean")
    private Set<ContractBean> contracts = new HashSet<>();
}
