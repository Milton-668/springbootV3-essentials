package academy.devdojo.springbootV3.exception;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
//obtêem um construtor para as classes filhas que vejam oque expõe as propriedades dos pais.
@SuperBuilder
public class ExceptionDetails {
    private String title;
    private int status;
    private String details;
    private String developerMessage;
    private LocalDateTime timestamp;
}
