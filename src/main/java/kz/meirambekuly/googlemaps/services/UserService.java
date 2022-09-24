package kz.meirambekuly.googlemaps.services;

import kz.meirambekuly.googlemaps.web.dto.ResponseDto;
import kz.meirambekuly.googlemaps.web.dto.UserCreatorDto;
import kz.meirambekuly.googlemaps.web.dto.UserDto;


public interface UserService {
    ResponseDto<?> findByEmail (String email);

    ResponseDto<?> findByUsername (String username);

    ResponseDto<?> register (UserCreatorDto dto);

    ResponseDto<?> getToken ();

    ResponseDto<?> getLoggedUserInformation ();

    ResponseDto<?> updateUser (UserDto dto);

    ResponseDto<?> changePassword(String oldPassword, String newPassword);
}
