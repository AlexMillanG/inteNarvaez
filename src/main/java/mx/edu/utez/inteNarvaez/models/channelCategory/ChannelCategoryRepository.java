package mx.edu.utez.inteNarvaez.models.channelCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChannelCategoryRepository extends JpaRepository<ChannelCategoryBean,Long> {

    Optional<ChannelCategoryBean> findByName(String s);

    Optional<ChannelCategoryBean> findByUuid(UUID uuid);
}
