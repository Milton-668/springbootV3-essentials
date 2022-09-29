package academy.devdojo.springbootV3.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BadRequestExceptionDetails {

    private String title;
    private int status;
    private String details;
    private String developerMessage;
    private LocalDateTime timestamp;
}
