package ru.suleymanov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.suleymanov.entity.User;
import ru.suleymanov.entity.UserRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByUserRoleInOrderByIdAsc(Iterable<UserRole> roles);

    List<User> findAllByUserRoleOrderByIdAsc(UserRole role);

    Optional<User> findByEmailIgnoreCase(String email);

    @Modifying
    @Query("UPDATE User SET userRole = :role WHERE id = :id")
    void updateUserRole(long id,@Param("role") UserRole role);
}
