package mx.edu.utez.inteNarvaez.config;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.controllers.auth.RegisterDTO;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;
import mx.edu.utez.inteNarvaez.models.channel.ChannelRepository;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryBean;
import mx.edu.utez.inteNarvaez.models.channelCategory.ChannelCategoryRepository;
import mx.edu.utez.inteNarvaez.models.role.RoleBean;
import mx.edu.utez.inteNarvaez.models.role.RoleRepository;
import mx.edu.utez.inteNarvaez.models.user.UserEntity;
import mx.edu.utez.inteNarvaez.services.security.services.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserServiceImpl userService;

    private final ChannelRepository channelRepository;
    private final RoleRepository roleRepository;


    @Bean
    @Transactional
    public CommandLineRunner initData() {
        return args -> {

            roles(new RoleBean("ADMIN", UUID.randomUUID()));
            roles(new RoleBean("USER", UUID.randomUUID()));


            createUser("admin","admin","admin","ASDFGHJKLASDF","admin1@admin.com","1234567890","1234");

            createCategoryChannel("Infantil");
            createCategoryChannel("Deportes");
            createCategoryChannel("Peliculas");

            createChannel("Nickelodeon","Un canal de caricaturas naranja",132,"https://upload.wikimedia.org/wikipedia/commons/thumb/e/e5/Nickelodeon_2023_logo_%28outline%29.svg/1200px-Nickelodeon_2023_logo_%28outline%29.svg.png",1L);
            createChannel("ESPN","ahí pasan futbol picante", 234,"https://static.wikia.nocookie.net/telepedia-es/images/9/99/ESPN_logo.png/revision/latest?cb=20210826204726&path-prefix=es",2L);
            createChannel("FOX", "nose, ya no lo veo",45435,"https://upload.wikimedia.org/wikipedia/commons/thumb/2/2b/Fox_Channel_logo.svg/2560px-Fox_Channel_logo.svg.png",3L);



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
            System.err.println("categoria de canal "+  name +" creada");
        }

    }

    private void roles(RoleBean role){
        Optional<RoleBean> foundRole = roleRepository.findByName(role.getName()); // Buscar por nombre

        if (foundRole.isEmpty()){
            roleRepository.save(role);
            System.err.println("Rol " + role.getName() + " creado correctamente");
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

    private void createUser(String name, String lastname, String surname, String rfc, String email, String phone,String password){
       Optional<UserEntity> foundAdmin = userRepository.findByEmail(email);
        if (foundAdmin.isEmpty()){
            UserEntity user = new UserEntity();
            user.setFirstName(name);
            user.setLastName(lastname);
            user.setSurname(surname);
            user.setRfc(rfc);
            user.setEmail(email);
            user.setPhone(phone);
            user.setBirthdate(new Date());
            user.setLastLogin(new Date());
            user.setStatus(true);
            user.setPassword(passwordEncoder.encode(password));
            user.setTemporalPassword(false);


            userRepository.save(user);
            System.err.println("Usuario " + name + " creado");
        } else {
            System.err.println("El usuario ya existe");
        }
    }




}