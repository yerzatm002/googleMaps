package kz.meirambekuly.googlemaps.repositories;

import kz.meirambekuly.googlemaps.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUsersByUsername (String username);

    Optional<User> findUsersByEmail (String email);

    User getUserByEmailAndPassword (String email, String password);
}