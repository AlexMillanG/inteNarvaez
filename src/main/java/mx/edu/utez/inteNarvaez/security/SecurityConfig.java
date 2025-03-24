package mx.edu.utez.inteNarvaez.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.services.security.repository.IJWTUtilityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@AllArgsConstructor
public class SecurityConfig {

    private final IJWTUtilityService jwtUtilityService;

    private final CustomUserDetailsService customUserDetailsService;

    private final PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/auth/**").permitAll() // Acceso libre a /auth/**
                                .requestMatchers("/api/channelPackage/**").hasRole("ADMIN") // Solo ADMIN puede acceder
                                .requestMatchers("/api/channelCategory/**").hasRole("ADMIN") // Solo ADMIN puede acceder
                                .requestMatchers("/api/channel/**").hasRole("ADMIN") // Solo ADMIN puede acceder
                                .requestMatchers("/api/user/**").hasRole("ADMIN") //  ADMIN pueden acceder
                                .requestMatchers("/api/product/**").hasRole("ADMIN") //  ADMIN pueden acceder

                                .requestMatchers("/api/contract/**").hasAnyRole("USER","ADMIN") // Solo USER puede acceder
                                .requestMatchers("/api/salesPackage/**").hasAnyRole("USER","ADMIN") // Solo ADMIN puede acceder
                                .requestMatchers("/api/address/**").hasAnyRole("USER","ADMIN") // Solo USER puede acceder
                                .requestMatchers("/api/client/**").hasAnyRole("USER", "ADMIN") //  ADMIN pueden acceder
                                .anyRequest().authenticated() // Rutas que requieren autenticación
                )
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JWTAuthorizationFilter(jwtUtilityService),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                        )
                )
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }
}
