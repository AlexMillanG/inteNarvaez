package mx.edu.utez.inteNarvaez.models.logo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoRepository extends JpaRepository<LogoBean,Long> {
}
