package mx.edu.utez.inteNarvaez.models.contract;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<ContractBean,Long> {

    Optional<ContractBean> findAllByUuid(UUID uuid);

    @Query(value = """
                SELECT DISTINCT c.email
                FROM contracts ct
                JOIN sales_packages sp ON ct.sales_package_id = sp.id
                JOIN channel_packages cp ON sp.channel_package_id = cp.id
                JOIN addresses a ON ct.address_id = a.id
                JOIN clients c ON a.client_id = c.id
                WHERE cp.id = :channelPackageId
               """, nativeQuery = true)

    List<String> findDistinctEmailsByChannelPackage(@Param("channelPackageId") Long channelPackageId);

}
