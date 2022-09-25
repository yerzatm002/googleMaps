package kz.meirambekuly.googlemaps.services.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import kz.meirambekuly.googlemaps.models.Location;
import kz.meirambekuly.googlemaps.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;
    private final LocationRepository locationRepository;

    public String sendNotification(Location location, String topic) throws FirebaseMessagingException {

        Location newLocation = locationRepository.save(location);

        Notification notification = Notification
                .builder()
                .setTitle(location.getActivity())
                .setBody(String.valueOf(location.getId()))
                .build();

        Map<String,String> locationPage = new HashMap<>();
        locationPage.put("pageNumber", String.valueOf(location.getPageNumber()));
        locationPage.put("longitude", String.valueOf(location.getLng()));
        locationPage.put("latitude", String.valueOf(location.getLat()));


        Message message = Message
                .builder()
                .setTopic(topic)
                .setNotification(notification)
                .putAllData(locationPage)
                .build();

        return firebaseMessaging.send(message);
    }

}
