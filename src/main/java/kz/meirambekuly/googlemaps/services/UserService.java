package kz.meirambekuly.googlemaps.services;

import kz.meirambekuly.googlemaps.web.dto.ResponseDto;
import kz.meirambekuly.googlemaps.web.dto.UserCreatorDto;
import kz.meirambekuly.googlemaps.web.dto.UserDto;
import kz.meirambekuly.googlemaps.web.dto.UserLoginDto;

import java.io.IOException;


public interface UserService {
    ResponseDto<?> findAllUsers();

    ResponseDto<?> findByEmail (String email);

    ResponseDto<?> findByUsername (String username);

    ResponseDto<?> signInWithGoogle(String authCode) throws IOException;

    ResponseDto<?> register (UserCreatorDto dto);

    ResponseDto<?> login (UserLoginDto dto);

    ResponseDto<?> getToken ();

    ResponseDto<?> getLoggedUserInformation ();

    ResponseDto<?> updateUser (UserCreatorDto dto);

    ResponseDto<?> changePassword(String oldPassword, String newPassword);
}
