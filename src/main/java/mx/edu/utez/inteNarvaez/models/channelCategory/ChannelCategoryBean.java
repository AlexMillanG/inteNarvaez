package mx.edu.utez.inteNarvaez.models.channelCategory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;

import java.util.*;

@Data
@Entity
@Table(name = "channel_categories")
public class ChannelCategoryBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(length = 36, unique = true)
    private UUID uuid;

    private Boolean status;


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ChannelBean> channels = new ArrayList<>();


    public ChannelCategoryBean() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }

    @PrePersist
    protected void onCreate() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }
}
