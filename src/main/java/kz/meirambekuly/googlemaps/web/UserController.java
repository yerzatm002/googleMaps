package kz.meirambekuly.googlemaps.web;

import io.swagger.annotations.ApiParam;
import kz.meirambekuly.googlemaps.services.UserService;
import kz.meirambekuly.googlemaps.web.dto.UserCreatorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/getLoggedUserInfo")
    public ResponseEntity<?> getLoggedUserInfo (){
        return ResponseEntity.ok(userService.getLoggedUserInformation());
    }

    @GetMapping("/getToken")
    @ApiParam(value = "get new token")
    public ResponseEntity<?> getToken(){
        return ResponseEntity.ok(userService.getToken());
    }

    @PutMapping("/updateUser")
    @ApiParam(value = "update user information")
    public ResponseEntity<?> updateEstablishment(@ApiParam(value = "user details")
                                                 @RequestBody UserCreatorDto dto){
        return ResponseEntity.ok(userService.updateUser(dto));
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam("oldPassword") String oldPassword,
                                           @RequestParam("newPassword") String newPassword) {
        return ResponseEntity.ok(userService.changePassword(oldPassword, newPassword));
    }

}
