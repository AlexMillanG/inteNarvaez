package mx.edu.utez.inteNarvaez.models.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageEntity;

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
    @Column(length = 36, unique = true)
    private UUID uuid;



    @OneToMany(mappedBy = "productBean")
    @JsonIgnore
    private Set<SalesPackageEntity> salesPackageEntities;


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
