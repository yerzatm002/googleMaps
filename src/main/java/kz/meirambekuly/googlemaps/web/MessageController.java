package kz.meirambekuly.googlemaps.web;

import com.google.firebase.messaging.FirebaseMessagingException;
import kz.meirambekuly.googlemaps.models.Location;
import kz.meirambekuly.googlemaps.services.LocationService;
import kz.meirambekuly.googlemaps.services.impl.FirebaseMessagingService;
import kz.meirambekuly.googlemaps.web.dto.Message;
import kz.meirambekuly.googlemaps.web.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ws")
@RequiredArgsConstructor
public class MessageController {

    private final FirebaseMessagingService firebaseService;

    @RequestMapping("/send-notification")
    @ResponseBody
    public String sendNotification(@RequestBody Location location,
                                   @RequestParam String topic) throws FirebaseMessagingException {
        return firebaseService.sendNotification(location, topic);
    }

//    @PostMapping("/danger")
//    public ResponseEntity<?> sendInDangerMessage(@RequestBody Location location){
//        return ResponseEntity.ok(locationService.saveLocationInDanger(location));
//    }


//    @MessageMapping("/message")
//    @SendTo("/topic/messages")
//    public ResponseMessage getMessage(final Message message) throws InterruptedException {
//        Thread.sleep(1000);
////        notificationService.sendGlobalNotification();
//        return new ResponseMessage(message.getLocation());
//    }
}
