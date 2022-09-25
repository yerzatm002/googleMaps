package kz.meirambekuly.googlemaps.web.dto;

import kz.meirambekuly.googlemaps.models.Location;

public class ResponseMessage {
    private Location location;

    public ResponseMessage() {
    }

    public ResponseMessage(Location content) {
        this.location = content;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
