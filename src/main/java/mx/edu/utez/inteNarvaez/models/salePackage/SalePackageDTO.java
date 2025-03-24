package mx.edu.utez.inteNarvaez.models.salePackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Data
@Getter
@Setter
@AllArgsConstructor
public class SalePackageDTO {
    private String name;
    private Double totalAmount;
    private UUID uuid;
    private String product_uuid;
    private String channel_package_uuid;
}
