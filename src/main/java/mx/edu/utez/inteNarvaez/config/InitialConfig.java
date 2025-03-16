package mx.edu.utez.inteNarvaez.config;

import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryBean;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryRepository;
import mx.edu.utez.inteNarvaez.models.role.RoleRepository;
import mx.edu.utez.inteNarvaez.models.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

    @Bean
    public CommandLineRunner initData() {
        return args -> {

            createCategoryChannel("Infantil");
            createCategoryChannel("Deportes");
            createCategoryChannel("Peliculas");

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



}