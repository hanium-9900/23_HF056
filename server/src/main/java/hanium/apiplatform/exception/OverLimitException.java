package hanium.apiplatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Api Usage Over Limit")

public class OverLimitException extends RuntimeException {

}
