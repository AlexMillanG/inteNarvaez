package mx.edu.utez.inteNarvaez.models.user;

import mx.edu.utez.inteNarvaez.models.user.UserEnitity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEnitity,Long> {

    @Query(value = "SELECT * FROM user where email = :email",nativeQuery = true)
    Optional<UserEnitity> findByEmail(String email);




}
