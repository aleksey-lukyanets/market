package market.exception;

/**
 * 
 */
public class RestNotAuthenticatedException extends Exception {
    
    public RestNotAuthenticatedException() {
        super("");
    }

    public RestNotAuthenticatedException(String message) {
        super(message);
    }
}
