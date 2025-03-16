package mx.edu.utez.inteNarvaez.models.address;

import mx.edu.utez.inteNarvaez.models.client.ClientBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<AddressBean,Long> {

    Optional<AddressBean> findByUuid(UUID uuid);

    List<AddressBean> findByClient(ClientBean clientBean);
}
