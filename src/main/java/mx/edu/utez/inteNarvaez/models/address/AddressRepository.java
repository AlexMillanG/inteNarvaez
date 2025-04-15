package mx.edu.utez.inteNarvaez.models.address;

import mx.edu.utez.inteNarvaez.models.client.ClientBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressBean,Long> {

    Optional<AddressBean> findByUuid(String uuid);

    Optional<AddressBean> findByName(String name);


    List<AddressBean> findByStatus(Boolean b);

    List<AddressBean> findByStatusAndClient(Boolean b, ClientBean clientBean);

}
