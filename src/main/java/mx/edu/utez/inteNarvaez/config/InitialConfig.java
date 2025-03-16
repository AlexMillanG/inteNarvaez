package mx.edu.utez.inteNarvaez.config;

import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;
import mx.edu.utez.inteNarvaez.models.channel.ChannelRepository;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryBean;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryRepository;
import mx.edu.utez.inteNarvaez.models.role.RoleRepository;
import mx.edu.utez.inteNarvaez.models.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.nio.channels.Channel;
import java.util.Optional;


@Component
public class InitialConfig {

    // Repositorios para acceder a los datos de roles, usuarios, imágenes, categorías, departamentos y puestos
    @Autowired
    private RoleRepository rolRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private ChannelCategoryRepository channelCategoryRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {

            createCategoryChannel("Infantil");
            createCategoryChannel("Deportes");
            createCategoryChannel("Peliculas");

            createChannel("Nickelodeon","Un canal de caricaturas naranja",132,"https://upload.wikimedia.org/wikipedia/commons/thumb/e/e5/Nickelodeon_2023_logo_%28outline%29.svg/1200px-Nickelodeon_2023_logo_%28outline%29.svg.png",1L);
            createChannel("ESPN","ahí pasan futbol picante", 234,"https://static.wikia.nocookie.net/telepedia-es/images/9/99/ESPN_logo.png/revision/latest?cb=20210826204726&path-prefix=es",2L);
            createChannel("FOX", "nose, ya no lo veo",45435,"https://upload.wikimedia.org/wikipedia/commons/thumb/2/2b/Fox_Channel_logo.svg/2560px-Fox_Channel_logo.svg.png",3L);

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

    private void createChannel(String name, String description, Integer number, String image, Long categoryId) {
        Optional<ChannelBean> foundChannel = channelRepository.findByName(name);
        if (foundChannel.isEmpty()) {
            Optional<ChannelCategoryBean> foundCategory = channelCategoryRepository.findById(categoryId);

            if (foundCategory.isPresent()) {
                ChannelBean channel = new ChannelBean();
                channel.setName(name);
                channel.setDescription(description);
                channel.setNumber(number);
                channel.setImage(image);
                channel.setCategory(foundCategory.get());  // Usamos la categoría existente de la BD

                channelRepository.save(channel);
                System.err.println("Canal " + name + " insertado");
            } else {
                System.err.println("Error: La categoría con ID " + categoryId + " no existe");
            }
        }
    }




}