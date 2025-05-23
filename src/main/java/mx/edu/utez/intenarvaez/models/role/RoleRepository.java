package mx.edu.utez.intenarvaez.models.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleBean,Long> {


    Optional<RoleBean> findByUuid(String uid);
    Optional<RoleBean> findByName(String name);



}
