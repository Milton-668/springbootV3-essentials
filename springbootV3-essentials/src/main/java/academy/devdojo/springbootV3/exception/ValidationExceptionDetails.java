package academy.devdojo.springbootV3.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
////obtêem um construtor para as classes filhas que vejam oque expõe as propriedades dos pais.
@SuperBuilder
public class ValidationExceptionDetails extends ExceptionDetails{
    private String fields;
    private String fieldsMessage;
}
