package in.snm.restservice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.mobileNo = ?1")
    Optional<User> findByMobileNo(String mobileNo);
}