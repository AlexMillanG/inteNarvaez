package mx.edu.utez.inteNarvaez.models.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductBean,Long> {

    Optional<ProductBean> findProductBeanByUuid(UUID uuid);
    Optional<ProductBean> findProductBeanByName(String name);
}
