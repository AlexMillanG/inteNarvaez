package mx.edu.utez.intenarvaez.models.channelPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ChannelPackageRepository extends JpaRepository<ChannelPackageBean,Long> {

    Optional<ChannelPackageBean> findChannelPackageBeanByUuid(String uuid);

    Optional<ChannelPackageBean> findChannelPackageBeanByNameAndStatus(String name,ChannelPackageStatus channelPackageStatus);

    List<ChannelPackageBean> findAllByStatus(ChannelPackageStatus status);

    Optional<Integer> countByStatus(ChannelPackageStatus channelPackageStatus);

}
