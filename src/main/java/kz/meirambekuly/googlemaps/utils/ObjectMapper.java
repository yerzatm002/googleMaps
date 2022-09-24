package kz.meirambekuly.googlemaps.utils;

import kz.meirambekuly.googlemaps.models.Role;
import kz.meirambekuly.googlemaps.models.User;
import kz.meirambekuly.googlemaps.web.dto.RoleDto;
import kz.meirambekuly.googlemaps.web.dto.UserDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectMapper {
    public static UserDto convertToUserDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(convertToRoleDto(user.getRole()))
                .build();
    }

    public static RoleDto convertToRoleDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

}
