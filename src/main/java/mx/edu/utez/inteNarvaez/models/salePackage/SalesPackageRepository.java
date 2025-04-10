package mx.edu.utez.inteNarvaez.models.salePackage;

import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SalesPackageRepository extends JpaRepository<SalesPackageEntity,Long> {

    Optional<SalesPackageEntity> findByUuid(UUID uid);
    Optional<SalesPackageEntity> findByName(String name);
    List<SalesPackageEntity> findByChannelPackage(ChannelPackageBean channelPackageBean);

    List<SalesPackageEntity> findByStatus(Boolean status);
}
