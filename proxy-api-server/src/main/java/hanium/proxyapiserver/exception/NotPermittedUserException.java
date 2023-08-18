package hanium.proxyapiserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "User not permitted for the action")
public class NotPermittedUserException extends RuntimeException {
}
