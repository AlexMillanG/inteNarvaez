package mx.edu.utez.inteNarvaez.models.salePackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesPackageRepository extends JpaRepository<SalesPackageEntity,Long> {
}
