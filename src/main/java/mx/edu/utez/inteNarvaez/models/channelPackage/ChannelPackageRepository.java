package mx.edu.utez.inteNarvaez.models.channelPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChannelPackageRepository extends JpaRepository<ChannelPackageBean,Long> {

    Optional<ChannelPackageBean> findByUuid(UUID uuid);
}
