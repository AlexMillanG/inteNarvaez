package mx.edu.utez.intenarvaez.models.channelPackage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.intenarvaez.models.channel.ChannelBean;
import mx.edu.utez.intenarvaez.models.salePackage.SalesPackageEntity;

import java.util.*;

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
    @Enumerated(EnumType.STRING)
    private ChannelPackageStatus status;
    @Column(length = 36, unique = true)
    private String uuid;


    @OneToMany(mappedBy = "channelPackage")
    @JsonIgnore
    private Set<SalesPackageEntity> salesPackages = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "channel_package_channels",
            joinColumns = @JoinColumn(name = "channel_package_id"),
            inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    @JsonManagedReference
    private List<ChannelBean> channels = new ArrayList<>();


    public ChannelPackageBean(Long id, String name, String description, List<ChannelBean> channels) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.channels = channels;
    }

    public ChannelPackageBean(String name, String description, List<ChannelBean> channels) {
        this.name = name;
        this.description = description;
        this.channels = channels;
    }

    public ChannelPackageBean() {

    }

    @PrePersist
    protected void onCreate() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }


}
