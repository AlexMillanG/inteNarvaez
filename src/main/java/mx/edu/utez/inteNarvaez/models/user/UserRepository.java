package mx.edu.utez.inteNarvaez.models.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    //@Query(value = "SELECT * FROM UserEntity where email = :email",nativeQuery = true)
 //   @Query(value = "SELECT * FROM user where email = :email",nativeQuery = true)

    Optional<UserEntity> findByEmail(String email);


    @Modifying
    @Transactional
  //  @Query(value = "INSERT INTO user_has_roles (user_id, role_id) VALUES (:userId, :roleId)", nativeQuery = true)
    void insertRoles(@Param("userId") Long userId, @Param("roleId") Long roleId);





}
