package kz.meirambekuly.googlemaps.web.dto;

import kz.meirambekuly.googlemaps.models.Location;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LocationsDto {
    private int pageNumber;
    private List<Location> locations;
}
