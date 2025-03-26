package mx.edu.utez.inteNarvaez.models.channelPackage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;
import mx.edu.utez.inteNarvaez.models.salePackage.SalesPackageEntity;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "channel_packages")
public class ChannelPackageBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, unique = true)
    private String name;
    @Column(length = 350)
    private String description;
    private Double amount;
    @Enumerated(EnumType.STRING)
    private ChannelPackageStatus status;
    @Column(length = 36, unique = true)
    private UUID uuid;

    @JsonIgnore
    @OneToMany(mappedBy = "channelPackage")
    private Set<SalesPackageEntity> salesPackages = new HashSet<>();


    @ManyToMany
    @JoinTable(
            name = "channel_package_channels",
            joinColumns = @JoinColumn(name = "channel_package_id"),
            inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    @JsonIgnore
    private Set<ChannelBean> channels = new HashSet<>();

    public ChannelPackageBean(Long id, String name, String description, Double amount, Set<ChannelBean> channels) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.channels = channels;
    }

    public ChannelPackageBean(String name, String description, Double amount, Set<ChannelBean> channels) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.channels = channels;
    }

    public ChannelPackageBean() {
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
