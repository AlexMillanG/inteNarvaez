package mx.edu.utez.inteNarvaez.models.channelCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelCategoryRepository extends JpaRepository<ChannelCategory,Long> {
}
