package mx.edu.utez.inteNarvaez.models.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleBean,Long> {


    Optional<RoleBean> findByUuid(UUID uid);
    Optional<RoleBean> findByName(String name);



}
