package kz.meirambekuly.googlemaps.web;

import kz.meirambekuly.googlemaps.services.UserService;
import kz.meirambekuly.googlemaps.web.dto.UserCreatorDto;
import kz.meirambekuly.googlemaps.web.dto.UserDto;
import kz.meirambekuly.googlemaps.web.dto.UserLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserCreatorDto dto){
        return ResponseEntity.ok(userService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody UserLoginDto dto){
        return ResponseEntity.ok(userService.login(dto));
    }

    @PostMapping("/google/login")
    public ResponseEntity<?> googleLogin(@RequestParam("idToken") String idToken) throws IOException {
        return ResponseEntity.ok(userService.signInWithGoogle(idToken));
    }
}
