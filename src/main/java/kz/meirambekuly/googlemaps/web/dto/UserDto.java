package kz.meirambekuly.googlemaps.web.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto{
    private Long id;
    private String email;
    private RoleDto role;
}
