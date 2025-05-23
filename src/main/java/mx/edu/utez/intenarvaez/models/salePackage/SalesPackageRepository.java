package mx.edu.utez.intenarvaez.models.salePackage;
import mx.edu.utez.intenarvaez.models.channelPackage.ChannelPackageBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesPackageRepository extends JpaRepository<SalesPackageEntity,Long> {

    Optional<SalesPackageEntity> findByUuid(String uid);
    Optional<SalesPackageEntity> findByName(String name);
    List<SalesPackageEntity> findByChannelPackage(ChannelPackageBean channelPackageBean);

    List<SalesPackageEntity> findByStatus(Boolean status);

    @Query("SELECT COUNT(c) FROM ContractBean c WHERE c.salesPackageEntity.id = :id")
    int countContractsBySalesPackageId(@Param("id") Long id);

    @Query("SELECT COUNT(c) > 0 FROM ContractBean c WHERE c.salesPackageEntity.id = :id")
    boolean existsContractsBySalesPackageId(@Param("id") Long id);

    Optional<Integer> countByStatus(Boolean c);
}
