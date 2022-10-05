package academy.devdojo.springbootV3.handler;

import academy.devdojo.springbootV3.exception.BadRequestException;
import academy.devdojo.springbootV3.exception.BadRequestExceptionDetails;
import academy.devdojo.springbootV3.exception.ValidationExceptionDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
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

    /*Nesse method há a customização do handler quando a Expcetion lançada for a seguinte:
     * MethodArgumentNotValidException, então é retornado um ValidationExceptionDetails que herda
     * da BadRequestExceptionDetails alguns atribtuso e mostrando a mensagem no stacktrace*/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionDetails> handlerMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        log.info("Field(s) {}", exception.getBindingResult().getFieldError().getField());
        //Cria uma lista de FieldError, a qual será percorrida para encontrar os resutlados de erros em cima dos campos
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        /*Faz uma varredura em cima do campo field, delimitando com uma vírgula para inserir o outro campo*/
        String fields = fieldErrors.stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(", "));
        /*Faz uma varredura em cima do campo fieldsMessage, delimitando com uma vírgula para inserir o outro campo*/
        String fieldsMessage = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(","));

        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception, Checked the Documentation")
                        .details("Check the field(s) error")
                        .developerMessage(exception.getClass().getName())
                        .fields(fields)
                        .fieldsMessage(fieldsMessage)
                        .build(), HttpStatus.BAD_REQUEST);
    }
}
