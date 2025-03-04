package mx.edu.utez.inteNarvaez.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //.allowedOrigins("**")
                .allowedOriginPatterns("*") // Permite cualquier origen

                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("origins", "Content.Type", "Accept", "Authorizations")
                .allowCredentials(true)
                .maxAge(3600);

        registry.addMapping("/auth/**")
                //.allowedOrigins("**")
                .allowedOriginPatterns("*") // Permite cualquier origen
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("origins", "Content.Type", "Accept", "Authorizations")
                .allowCredentials(false)
                .maxAge(3600);
    }

}
