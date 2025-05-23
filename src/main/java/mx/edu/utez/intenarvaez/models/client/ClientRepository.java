package mx.edu.utez.intenarvaez.models.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ClientRepository extends JpaRepository<ClientBean,Long> {

    Optional<ClientBean> findByRfc(String rfc);

    Optional<ClientBean> findByUuid(String uuid);

    List<ClientBean> findByStatus(Boolean b);

    Optional<ClientBean> findByEmail(String s);

    Optional<Integer> countByStatus(Boolean b);
}
