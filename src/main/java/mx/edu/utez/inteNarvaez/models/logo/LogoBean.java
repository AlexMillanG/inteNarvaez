package mx.edu.utez.inteNarvaez.models.logo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "logos")
@Data
@ToString(exclude = "channel")

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
    @JsonIgnore  // Evita que se serialice el canal de vuelta

    @JoinColumn(name = "channel_id", referencedColumnName = "id")
    private ChannelBean channel;


    @Override
    public int hashCode() {
        return Objects.hash(this.id); // Solo el ID
    }
}
