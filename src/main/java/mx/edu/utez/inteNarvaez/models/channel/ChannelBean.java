package mx.edu.utez.inteNarvaez.models.channel;

import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategory;

@Data
@Table(name = "channels")
@Entity
public class ChannelBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Integer number;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ChannelCategory category;
}
