package kz.meirambekuly.googlemaps.web.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatorDto {
    private String email;
    private String username;
    private String password;
}

