package mx.edu.utez.intenarvaez.models.logo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogoRepository extends JpaRepository<LogoBean,Long> {

    Optional<LogoBean> findByChannel_Id(Long id);
}
