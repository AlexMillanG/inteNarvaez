package mx.edu.utez.inteNarvaez.models.channelPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelPackageRepository extends JpaRepository<ChannelPackageBean,Long> {
}
