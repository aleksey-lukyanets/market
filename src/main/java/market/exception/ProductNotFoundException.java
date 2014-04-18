package market.exception;

/**
 * 
 */
public class ProductNotFoundException extends Exception {
    
    public ProductNotFoundException() {
        super("");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
