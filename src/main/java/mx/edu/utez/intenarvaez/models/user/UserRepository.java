package mx.edu.utez.intenarvaez.models.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {


    Optional<UserEntity> findByEmail(String email);



    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_has_roles (user_id, role_id) VALUES (:userId, :roleId)", nativeQuery = true)
    void insertRoles(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_has_roles WHERE user_id = :userId AND role_id = :roleId", nativeQuery = true)
    void deleteUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);




}