package kz.meirambekuly.googlemaps.repositories;

import kz.meirambekuly.googlemaps.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findLocationByPageNumber (Integer pageNumber);
    List<Location> findLocationByIdentifier (String identifier);
}