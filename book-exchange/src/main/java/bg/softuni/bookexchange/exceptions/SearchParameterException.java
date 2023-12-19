package bg.softuni.bookexchange.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SearchParameterException extends IllegalArgumentException{

    public SearchParameterException(String msg) {
        super(msg);
    }
}
