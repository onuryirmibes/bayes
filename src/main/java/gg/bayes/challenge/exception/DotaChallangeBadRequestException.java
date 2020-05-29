package gg.bayes.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DotaChallangeBadRequestException extends RuntimeException {

    public DotaChallangeBadRequestException(final String message) {
        super(message);
    }
}
