package hanium.apiplatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Duplicate Service Key for User")
public class DuplicateServiceKeyException extends RuntimeException {
}
