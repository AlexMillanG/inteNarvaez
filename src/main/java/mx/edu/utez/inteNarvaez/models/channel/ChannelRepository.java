package mx.edu.utez.inteNarvaez.models.channel;

import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChannelRepository extends JpaRepository<ChannelBean,Long> {

    Optional<ChannelBean> findByNumberAndStatus(Integer number, Boolean b);
    Optional<ChannelBean> findByName(String name);
    List<ChannelBean> findByCategoryAndStatus(ChannelCategoryBean category, Boolean b);
    Optional<ChannelBean> findByUuid(UUID uuid);

    List<ChannelBean> findByStatus(Boolean b);
}
