package mx.edu.utez.inteNarvaez.models.salePackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SalesPackageRepository extends JpaRepository<SalesPackageEntity,Long> {

    Optional<SalesPackageEntity> findByUuid(UUID uid);
}
