package kz.meirambekuly.googlemaps.services.impl;

import kz.meirambekuly.googlemaps.models.Location;
import kz.meirambekuly.googlemaps.repositories.LocationRepository;
import kz.meirambekuly.googlemaps.services.LocationService;
import kz.meirambekuly.googlemaps.web.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public ResponseDto<?> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        return ResponseDto.builder()
                .isSuccess(true)
                .httpStatus(HttpStatus.OK.value())
                .data(locations)
                .build();
    }

    @Override
    public ResponseDto<?> getLocationsByPageNumber(Integer pageNumber) {
        if(pageNumber<=0){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .errorMessage("Invalid page number")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
        List<Location> locations = locationRepository.findLocationByPageNumber(pageNumber);
        return ResponseDto.builder()
                .isSuccess(true)
                .httpStatus(HttpStatus.OK.value())
                .data(locations)
                .build();
    }

    @Override
    public ResponseDto<?> getLocationsByIdentifier(String identifier) {
        if(identifier.isEmpty() || identifier.isBlank()){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .errorMessage("Invalid identifier")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
        List<Location> locations = locationRepository.findLocationByIdentifier(identifier);
        return ResponseDto.builder()
                .isSuccess(true)
                .httpStatus(HttpStatus.OK.value())
                .data(locations)
                .build();
    }

    @Override
    public ResponseDto<?> saveLocation(Location location) {
        Location newLocation = locationRepository.save(location);
        return ResponseDto.builder()
                .isSuccess(true)
                .httpStatus(HttpStatus.OK.value())
                .data(newLocation)
                .build();
    }

//    @Override
//    public ResponseDto<?> updateLocation(Location location) {
//        return null;
//    }
}
