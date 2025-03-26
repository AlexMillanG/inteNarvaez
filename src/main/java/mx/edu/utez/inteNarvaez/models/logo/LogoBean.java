package mx.edu.utez.inteNarvaez.models.logo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;

import java.util.UUID;

@Entity
@Table(name = "logos")
@Data
public class LogoBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    private String fileExtension;

    private UUID uuid;




    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "channel_id", referencedColumnName = "id")
    private ChannelBean channel;
}
