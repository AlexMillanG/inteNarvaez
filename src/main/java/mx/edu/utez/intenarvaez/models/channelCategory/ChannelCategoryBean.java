package mx.edu.utez.intenarvaez.models.channelCategory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import mx.edu.utez.intenarvaez.models.channel.ChannelBean;

import java.util.*;

@Data
@Entity
@Table(name = "channel_categories")
public class ChannelCategoryBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la categoría no puede estar vacío.")
    @Size(max = 100, message = "El nombre de la categoría no puede exceder los 100 caracteres.")
    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 36, unique = true)
    private String uuid;

    private Boolean status;


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ChannelBean> channels = new ArrayList<>();


    public ChannelCategoryBean() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }

    @PrePersist
    protected void onCreate() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }
}
