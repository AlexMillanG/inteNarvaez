package mx.edu.utez.intenarvaez.models.BitacoraAcceso;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BitacoraAccesoRepository extends JpaRepository<BitacoraAccesoEntity, Long> {

    List<BitacoraAccesoEntity> findAllByUserId(Long userId);
}
