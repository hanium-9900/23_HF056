package hanium.apiplatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "API is Not Valid")
public class NotValidException extends RuntimeException {

}
