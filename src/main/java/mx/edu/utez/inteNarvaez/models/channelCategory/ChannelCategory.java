package mx.edu.utez.inteNarvaez.models.channelCategory;

import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;

import java.util.Set;

@Data
@Entity
@Table(name = "channel_categories")
public class ChannelCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChannelBean> channels;
}
