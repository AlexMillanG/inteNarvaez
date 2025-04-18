package mx.edu.utez.intenarvaez.models.address;

import mx.edu.utez.intenarvaez.models.client.ClientBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressBean,Long> {


    Optional<AddressBean> findByNameAndClient_Id(String name, Long clientId);

    Optional<AddressBean> findByUuid(String uuid);

    List<AddressBean> findByStatus(Boolean b);

    List<AddressBean> findByStatusAndClient(Boolean b, ClientBean clientBean);

}
