package com.arnav.sms.dto.api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// api Response generic class meaning ye responseDTO requestDTO string etc type fitted hai
// when api returns OK to success msg
// otherwise error msg
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(String message , T data) {
        return new ApiResponse<T>(true , message , data , LocalDateTime.now());
    }
    public static <T> ApiResponse<T> error(String message , T data) {
        return new ApiResponse<T>(true , message , null, LocalDateTime.now());
    }

}
