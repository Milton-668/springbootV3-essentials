package academy.devdojo.springbootV3.handler;

import academy.devdojo.springbootV3.exception.BadRequestException;
import academy.devdojo.springbootV3.exception.BadRequestExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {
    /*Classe utilizada para personalizar uma exception quando for uma BadRequestException*/

    /*Método responsável por personalizar a exception apresentada no stackTrace, onde que será
    * retornado uma ResponseEntity com o contéudo dos atributos da classe BadRequestExceptionDetails
    * e por fim o seu status -> BAD_REQUEST*/
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException bre) {
        return new ResponseEntity<>(
                BadRequestExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception, Checked the Documentation")
                        .details(bre.getMessage())
                        .developerMessage(bre.getClass().getName())
                        .build(), HttpStatus.BAD_REQUEST);
    }
}
