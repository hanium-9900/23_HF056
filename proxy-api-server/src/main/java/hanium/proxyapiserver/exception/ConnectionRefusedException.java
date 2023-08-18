package hanium.proxyapiserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Connection Refused")
public class ConnectionRefusedException extends RuntimeException {

}
