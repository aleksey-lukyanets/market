package market.exception;

/**
 * Попытка сослаться на неизвестный товар.
 */
public class UnknownProductException extends CustomNotValidException {

    public UnknownProductException() {
        super("NotExist", "product", "id");
    }
}
