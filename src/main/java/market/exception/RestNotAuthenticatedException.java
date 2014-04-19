package market.exception;

/**
 * Пользователь REST-службы не авторизован.
 */
public class RestNotAuthenticatedException extends Exception {
    
    public RestNotAuthenticatedException() {
        super("");
    }

    public RestNotAuthenticatedException(String message) {
        super(message);
    }
}
