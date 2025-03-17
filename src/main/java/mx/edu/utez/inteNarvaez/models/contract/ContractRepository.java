package mx.edu.utez.inteNarvaez.models.contract;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<ContractBean,Long> {

    Optional<ContractBean> findAllByUuid(UUID uuid);
}
