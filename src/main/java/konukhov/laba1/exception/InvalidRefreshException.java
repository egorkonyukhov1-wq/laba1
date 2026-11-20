package konukhov.laba1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidRefreshException extends RuntimeException {
    public InvalidRefreshException(String reason) { super(reason); }
}