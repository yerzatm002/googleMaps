package kz.meirambekuly.googlemaps.web;

import kz.meirambekuly.googlemaps.services.UserService;
import kz.meirambekuly.googlemaps.web.dto.UserCreatorDto;
import kz.meirambekuly.googlemaps.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserCreatorDto dto){
        return ResponseEntity.ok(userService.register(dto));
    }

}
