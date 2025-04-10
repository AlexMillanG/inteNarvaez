package mx.edu.utez.inteNarvaez.models.contract;

import mx.edu.utez.inteNarvaez.models.address.AddressBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<ContractBean,Long> {

    Optional<ContractBean> findAllByUuid(UUID uuid);

    List<ContractBean> findByAddress(AddressBean addressBean);

    List<ContractBean> findByStatus(Boolean status);
}
