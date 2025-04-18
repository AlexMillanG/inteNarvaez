package mx.edu.utez.intenarvaez.config;

import lombok.AllArgsConstructor;
import mx.edu.utez.intenarvaez.controllers.auth.RegisterDTO;
import mx.edu.utez.intenarvaez.controllers.channel.dto.ChannelDTO;
import mx.edu.utez.intenarvaez.models.channel.ChannelBean;
import mx.edu.utez.intenarvaez.models.channel.ChannelRepository;
import mx.edu.utez.intenarvaez.models.channelCategory.ChannelCategoryBean;
import mx.edu.utez.intenarvaez.models.channelCategory.ChannelCategoryRepository;
import mx.edu.utez.intenarvaez.models.role.RoleBean;
import mx.edu.utez.intenarvaez.models.role.RoleRepository;
import mx.edu.utez.intenarvaez.services.channel.ChannelService;
import mx.edu.utez.intenarvaez.services.security.services.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.ClassPathResource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.mock.web.MockMultipartFile;


@AllArgsConstructor
@Component
public class InitialConfig {

    private final ChannelCategoryRepository channelCategoryRepository;
    private final UserServiceImpl userService;
    private static final Logger logger = LogManager.getLogger(InitialConfig.class);
    private final ChannelRepository channelRepository;
    private final ChannelService channelService;
    private final RoleRepository roleRepository;

    @Bean
    @Transactional
    public CommandLineRunner initData() {
        return args -> {
             String image ="image";

            roles(new RoleBean("ADMIN", UUID.randomUUID().toString()));
            roles(new RoleBean("USER", UUID.randomUUID().toString()));

            createCategoryChannel("Infantil");
            createCategoryChannel("Deportes");
            createCategoryChannel("Peliculas");

            ChannelDTO dto = new ChannelDTO();
            dto.setName("Nickelodeon");
            dto.setDescription("Un canal de caricaturas naranja");
            dto.setNumber(132);
            dto.setCategoryId(1L);

            ClassPathResource imageFile = new ClassPathResource("image/nik.png");
            MockMultipartFile multipartFile = new MockMultipartFile(
                    image,
                    imageFile.getFilename(),
                    "image/png",
                    imageFile.getInputStream()
            );
            dto.setImage(multipartFile);

            channelService.saveWithImage(dto);

            ChannelDTO dtoESPN = new ChannelDTO();
            dtoESPN.setName("ESPN");
            dtoESPN.setDescription("Ahí pasan futbol picante");
            dtoESPN.setNumber(234);
            dtoESPN.setCategoryId(2L);

            ClassPathResource imageFileESPN = new ClassPathResource("image/espn.png");
            MockMultipartFile multipartFileESPN = new MockMultipartFile(
                    image, imageFileESPN.getFilename(), "image/png",
                    imageFileESPN.getInputStream()
            );

            dtoESPN.setImage(multipartFileESPN);
            channelService.saveWithImage(dtoESPN);
            ChannelDTO dtoFOX = new ChannelDTO();
            dtoFOX.setName("FOX");
            dtoFOX.setDescription("No se ya no lo veo");
            dtoFOX.setNumber(454);
            dtoFOX.setCategoryId(3L);
            ClassPathResource imageFileFOX = new ClassPathResource("image/fox.png");
            MockMultipartFile multipartFileFOX = new MockMultipartFile(
                    image, imageFileFOX.getFilename(), "/image/png", imageFileFOX.getInputStream()
            );
            dtoFOX.setImage(multipartFileFOX);
            channelService.saveWithImage(dtoFOX);


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


    private void createCategoryChannel(String name) {
        Optional<ChannelCategoryBean> foundChannelCategory = channelCategoryRepository.findByName(name);
        if (foundChannelCategory.isEmpty()) {
            ChannelCategoryBean categoryBean = new ChannelCategoryBean();
            categoryBean.setName(name);
            categoryBean.setStatus(true);
            channelCategoryRepository.save(categoryBean);
            logger.info("Creating channel category: {}", name);

        }

    }

    private void roles(RoleBean role) {
        Optional<RoleBean> foundRole = roleRepository.findByName(role.getName());

        if (foundRole.isEmpty()) {
            roleRepository.save(role);
            logger.info("Creando rol: {}", role.getName());
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

                logger.info("Canal {} insertado", name);
            } else {

                logger.error("Error: La categoría con ID {} no existe", categoryId);
            }
        }
    }


}