package mx.edu.utez.inteNarvaez.models.channel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryBean;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageBean;
import mx.edu.utez.inteNarvaez.models.logo.LogoBean;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    private Boolean status;

    @Column(length = 36, unique = true)
    private UUID uuid;

    @Column(columnDefinition = "TEXT")
    private String image;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "category_id", nullable = false)
    private ChannelCategoryBean category;


    @ManyToMany(mappedBy = "channels")
    @JsonIgnore
    private Set<ChannelPackageBean> channelPackages = new HashSet<>();


    @OneToOne(mappedBy = "channel")
    private LogoBean logoBean;


    public ChannelBean() {
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
