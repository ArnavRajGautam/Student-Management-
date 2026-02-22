package com.arnav.sms.exceptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
// Custom Error Response class More flexibility
public class ErrorResponseWrapper {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    // for validation errors ( puts wht caused the error and error msg )
    private Map<String, String> validationErrors;

    // for custom exceptions
    // this constructor is used to assign errorLog msg in GlobalExceptionHandler class

    public ErrorResponseWrapper(LocalDateTime timestamp, int status, String error,
                                String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

}
