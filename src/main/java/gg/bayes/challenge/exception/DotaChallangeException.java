package gg.bayes.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DotaChallangeException extends RuntimeException {

    public DotaChallangeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DotaChallangeException(final String message) {
        super(message);
    }
}
