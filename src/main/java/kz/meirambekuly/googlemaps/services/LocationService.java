package kz.meirambekuly.googlemaps.services;


import kz.meirambekuly.googlemaps.models.Location;
import kz.meirambekuly.googlemaps.web.dto.ResponseDto;

public interface LocationService {
    ResponseDto<?> getAllLocations();

    ResponseDto<?> getLocationsByPageNumber(Integer pageNumber);

    ResponseDto<?> getLocationsByIdentifier(String identifier);

    ResponseDto<?> saveLocation(Location location);

//    ResponseDto<?> updateLocation(Location location);
}
