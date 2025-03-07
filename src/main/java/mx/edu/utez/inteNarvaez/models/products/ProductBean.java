package mx.edu.utez.inteNarvaez.models.products;

import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageBean;

import java.util.Set;
import java.util.UUID;

@Data
@Table(name = "products")
@Entity
public class ProductBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String speed;

    private Double amount;

    private String description;

    private UUID uuid;



    @OneToMany(mappedBy = "productBean")
    private Set<SalesPackageBean> salesPackageBeans;


    public ProductBean() {
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
