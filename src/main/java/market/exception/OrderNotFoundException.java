package market.exception;

/**
 * Запрошенный заказ не найден.
 */
public class OrderNotFoundException extends Exception {
    
    public OrderNotFoundException() {
        super("");
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
