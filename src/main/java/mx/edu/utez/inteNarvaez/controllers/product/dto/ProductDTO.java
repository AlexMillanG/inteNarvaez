package mx.edu.utez.inteNarvaez.controllers.product.dto;

import lombok.Data;
import mx.edu.utez.inteNarvaez.models.products.ProductBean;

import java.util.UUID;

@Data
public class ProductDTO {

    private Long id;
    private String name;
    private String speed;
    private Double amount;
    private String description;
    private UUID uuid;

    public ProductBean toEntity(){
        ProductBean productBean = new ProductBean();
        productBean.setId(this.id);
        productBean.setName(this.name);
        productBean.setSpeed(this.speed);
        productBean.setAmount(this.amount);
        productBean.setDescription(this.description);
        productBean.setUuid(this.uuid);
        return productBean;
    }
}
