package mx.edu.utez.inteNarvaez.models.channelCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelCategoryRepository extends JpaRepository<ChannelCategoryBean,Long> {

    Optional<ChannelCategoryBean> findByName(String s);

    Optional<ChannelCategoryBean> findByUuid(String uuid);

    List<ChannelCategoryBean> findByStatus(Boolean b);
}
