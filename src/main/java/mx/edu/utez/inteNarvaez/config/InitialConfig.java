package mx.edu.utez.inteNarvaez.config;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.controllers.auth.RegisterDTO;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;
import mx.edu.utez.inteNarvaez.models.channel.ChannelRepository;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryBean;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryRepository;
import mx.edu.utez.inteNarvaez.models.role.RoleBean;
import mx.edu.utez.inteNarvaez.models.role.RoleRepository;
import mx.edu.utez.inteNarvaez.services.channel.ChannelService;
import mx.edu.utez.inteNarvaez.services.security.services.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@AllArgsConstructor
@Component
public class InitialConfig {

    private final ChannelCategoryRepository channelCategoryRepository;
    private static final Logger logger = LogManager.getLogger(InitialConfig.class);
    private final UserServiceImpl userService;
    private final ChannelRepository channelRepository;
    private final RoleRepository roleRepository;


    @Bean
    @Transactional
    public CommandLineRunner initData() {
        return args -> {

            roles(new RoleBean("ADMIN", UUID.randomUUID()));
            roles(new RoleBean("USER", UUID.randomUUID()));



            createCategoryChannel("Infantil");
            createCategoryChannel("Deportes");
            createCategoryChannel("Peliculas");

            createChannel("Nickelodeon","Un canal de caricaturas naranja",132,1L);
            createChannel("ESPN","ahí pasan futbol picante", 234,2L);
            createChannel("FOX", "nose, ya no lo veo",45435,3L);



            RegisterDTO crateUserAdmin = new RegisterDTO();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthdate = sdf.parse("2000-01-16");
            crateUserAdmin.setFirstName("Juan");
            crateUserAdmin.setLastName("Perez");
            crateUserAdmin.setSurname("Cardenas");
            crateUserAdmin.setEmail("juanCardens@example.com");
            crateUserAdmin.setRfc("JPCG950215H22");
            crateUserAdmin.setPhone("7774236328");
            crateUserAdmin.setBirthdate(birthdate);
            crateUserAdmin.setRole("ADMIN");
            crateUserAdmin.setPassword("Segura#2024");
            userService.registerAgente(crateUserAdmin.toUserEntity());

            RegisterDTO createUser = new RegisterDTO();
            SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
            Date birthdateUser = sdff.parse("2004-01-16");
            createUser.setFirstName("Valeria");
            createUser.setLastName("Santos");
            createUser.setSurname("Delgado");
            createUser.setEmail("valeria.s.delgado@example.com");
            createUser.setRfc("SADV910305MZ2");
            createUser.setPhone("5567891234");
            createUser.setBirthdate(birthdateUser);
            createUser.setRole("USER");
            createUser.setPassword("Valeria#2025");
            userService.registerAgente(createUser.toUserEntity());


        };


    }


    private void createCategoryChannel(String name){
        Optional<ChannelCategoryBean> foundChannelCategory = channelCategoryRepository.findByName(name);
        if (foundChannelCategory.isEmpty()){
            ChannelCategoryBean categoryBean = new ChannelCategoryBean();
            categoryBean.setName(name);
            categoryBean.setStatus(true);
            channelCategoryRepository.save(categoryBean);
            logger.info("Categoría "+categoryBean.getName()+" creada");
        }

    }

    private void roles(RoleBean role){
        Optional<RoleBean> foundRole = roleRepository.findByName(role.getName()); // Buscar por nombre

        if (foundRole.isEmpty()){
            roleRepository.save(role);
            logger.info("rol "+ role.getName()+" creado");
        }
    }

    private void createChannel(String name, String description, Integer number, Long categoryId) {
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
                logger.info("canal "+ channel.getName()+" creado");
            } else {
                logger.info("la categoría con id "+categoryId+" no existe");

            }
        }
    }



}