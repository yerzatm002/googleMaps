package kz.meirambekuly.googlemaps.repositories;

import kz.meirambekuly.googlemaps.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User getUsersByUsername (String username);
}