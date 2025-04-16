package mx.edu.utez.intenarvaez.models.channel;

import mx.edu.utez.intenarvaez.models.channelCategory.ChannelCategoryBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<ChannelBean,Long> {

    Optional<ChannelBean> findByNumberAndStatus(Integer number, Boolean b);
    Optional<ChannelBean> findByName(String name);
    List<ChannelBean> findByCategoryAndStatus(ChannelCategoryBean category, Boolean b);
    Optional<ChannelBean> findByUuid(String uuid);
    List<ChannelBean> findByStatus(Boolean b);
    Optional<Integer> countByStatus(Boolean b);
}
