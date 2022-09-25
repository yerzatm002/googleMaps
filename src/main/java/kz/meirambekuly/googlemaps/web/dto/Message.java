package kz.meirambekuly.googlemaps.web.dto;

import kz.meirambekuly.googlemaps.models.Location;

public class Message {
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
