package mx.edu.utez.inteNarvaez.config;

import mx.edu.utez.inteNarvaez.models.user.usersValidation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ValidationsConfig {

    @Bean
    public  usersValidation usersValidation(){
        return new usersValidation();
    }

}
