package mx.edu.utez.inteNarvaez.models.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<ClientBean,Long> {

    Optional<ClientBean> findByRfc(String rfc);

    Optional<ClientBean> findByUuid(String uuid);
}
