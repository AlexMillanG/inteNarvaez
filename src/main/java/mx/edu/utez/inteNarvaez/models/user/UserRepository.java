package mx.edu.utez.inteNarvaez.models.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    @Query(value = "SELECT * FROM user where email = :email",nativeQuery = true)
    Optional<UserEntity> findByEmail(String email);




}
