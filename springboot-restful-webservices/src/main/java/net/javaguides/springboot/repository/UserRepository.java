package net.javaguides.springboot.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //@Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<User> findById(long id);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User getUserByUsername(@Param("username") String username);

}
