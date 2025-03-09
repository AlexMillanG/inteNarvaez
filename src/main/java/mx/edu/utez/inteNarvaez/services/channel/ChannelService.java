package mx.edu.utez.inteNarvaez.services.channel;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.models.channel.ChannelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
@Transactional(rollbackFor = SQLException.class)
@AllArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;


}
