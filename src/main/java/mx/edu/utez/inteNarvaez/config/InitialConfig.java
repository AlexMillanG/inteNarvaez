package mx.edu.utez.inteNarvaez.config;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;
import mx.edu.utez.inteNarvaez.models.channel.ChannelRepository;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryBean;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryRepository;
import mx.edu.utez.inteNarvaez.models.role.RoleBean;
import mx.edu.utez.inteNarvaez.models.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
public class InitialConfig {

    private final ChannelCategoryRepository channelCategoryRepository;
    private final ChannelRepository channelRepository;
    private final RoleRepository roleRepository;


    @Bean
    public CommandLineRunner initData() {
        return args -> {

            createCategoryChannel("Infantil");
            createCategoryChannel("Deportes");
            createCategoryChannel("Peliculas");

            createChannel("Nickelodeon","Un canal de caricaturas naranja",132,"https://upload.wikimedia.org/wikipedia/commons/thumb/e/e5/Nickelodeon_2023_logo_%28outline%29.svg/1200px-Nickelodeon_2023_logo_%28outline%29.svg.png",1L);
            createChannel("ESPN","ahí pasan futbol picante", 234,"https://static.wikia.nocookie.net/telepedia-es/images/9/99/ESPN_logo.png/revision/latest?cb=20210826204726&path-prefix=es",2L);
            createChannel("FOX", "nose, ya no lo veo",45435,"https://upload.wikimedia.org/wikipedia/commons/thumb/2/2b/Fox_Channel_logo.svg/2560px-Fox_Channel_logo.svg.png",3L);

            roles(new RoleBean("ADMIN", UUID.randomUUID()));
            roles(new RoleBean("USER", UUID.randomUUID()));

        };
    }


    private void createCategoryChannel(String name){
        Optional<ChannelCategoryBean> foundChannelCategory = channelCategoryRepository.findByName(name);
        if (foundChannelCategory.isEmpty()){
            ChannelCategoryBean categoryBean = new ChannelCategoryBean();
            categoryBean.setName(name);
            channelCategoryRepository.save(categoryBean);
            System.err.println("categoria de canal "+  name +" creada");
        }

    }

    private void roles(RoleBean role){
        Optional<RoleBean> foundRole = roleRepository.findByName(role.getName()); // Buscar por nombre

        if (foundRole.isEmpty()){
            roleRepository.save(role);
            System.err.println("Rol " + role.getName() + " creado correctamente");
        } else {
            System.err.println("Rol " + role.getName() + " ya existe");
        }
    }




    private void createChannel(String name, String description, Integer number, String image, Long categoryId) {
        Optional<ChannelBean> foundChannel = channelRepository.findByName(name);
        if (foundChannel.isEmpty()) {
            Optional<ChannelCategoryBean> foundCategory = channelCategoryRepository.findById(categoryId);

            if (foundCategory.isPresent()) {
                ChannelBean channel = new ChannelBean();
                channel.setName(name);
                channel.setDescription(description);
                channel.setNumber(number);
                channel.setCategory(foundCategory.get());

                channelRepository.save(channel);
                System.err.println("Canal " + name + " insertado");
            } else {
                System.err.println("Error: La categoría con ID " + categoryId + " no existe");
            }
        }
    }




}