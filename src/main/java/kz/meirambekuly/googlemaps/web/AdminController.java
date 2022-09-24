package kz.meirambekuly.googlemaps.web;

import kz.meirambekuly.googlemaps.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/getUserByEmail")
    public ResponseEntity<?> getUserByEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/getUserByUsername")
    public ResponseEntity<?> getUserByUsername(@RequestParam("username") String username){
        return ResponseEntity.ok(userService.findByUsername(username));
    }
}
