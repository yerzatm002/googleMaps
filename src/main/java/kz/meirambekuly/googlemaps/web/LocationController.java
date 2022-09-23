package kz.meirambekuly.googlemaps.web;

import kz.meirambekuly.googlemaps.models.Location;
import kz.meirambekuly.googlemaps.services.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api//location")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/findAll")
    public ResponseEntity<?> findAllLocations(){
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @GetMapping("/findByPageNumber")
    public ResponseEntity<?> findByPageNumber(@RequestParam(value = "pageNumber") Integer pageNumber){
        return ResponseEntity.ok(locationService.getLocationsByPageNumber(pageNumber));
    }

    @GetMapping("/findByIdentifier")
    public ResponseEntity<?> findByIdentifier(@RequestParam(value = "identifier") String identifier){
        return ResponseEntity.ok(locationService.getLocationsByIdentifier(identifier));
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Location location){
        return ResponseEntity.ok(locationService.saveLocation(location));
    }

}
