package mx.edu.utez.inteNarvaez.security;

import lombok.AllArgsConstructor;
import mx.edu.utez.inteNarvaez.models.role.RoleBean;
import mx.edu.utez.inteNarvaez.models.role.RoleDTO;  // Importar RoleDTO
import mx.edu.utez.inteNarvaez.models.user.UserEntity;
import mx.edu.utez.inteNarvaez.models.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Codificador de contraseñas
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar el usuario por su correo electrónico
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Mapear los roles a autoridades (authorities) de Spring Security
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (RoleDTO role : mapRolesToDTO(userEntity)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())); // Asignamos el prefijo "ROLE_" a los roles
        }

        // Crear y retornar un objeto User de Spring Security con las autoridades
        return new User(
                userEntity.getEmail(),               // username
                userEntity.getPassword(),            // password
                authorities                         // roles o autoridades
        );
    }

    // Método para mapear los roles del UserEntity a RoleDTO
    private Set<RoleDTO> mapRolesToDTO(UserEntity userEntity) {
        Set<RoleDTO> roleDTOs = new HashSet<>();
        for (RoleBean role : userEntity.getRoleBeans()) {
            roleDTOs.add(new RoleDTO(role.getId(), role.getName(), role.getUuid()));
        }
        return roleDTOs;
    }
}
