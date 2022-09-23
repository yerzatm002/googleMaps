package kz.meirambekuly.googlemaps.web.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    @Builder.Default
    private int httpStatus = 200;
    private boolean isSuccess;
    private String errorMessage;
    private T data;
}
