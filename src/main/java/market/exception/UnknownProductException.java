package market.exception;

import org.springframework.validation.FieldError;

/**
 * Попытка сослаться на неизвестный товар.
 */
public class UnknownProductException extends Exception {
    
    public UnknownProductException() {
        super("");
    }

    public UnknownProductException(String message) {
        super(message);
    }
    
    public FieldError getFieldError() {
        String[] codes = {"NotExist.product"};
        Object[] arguments = {"id"};
        return new FieldError("product", "id", "", false, codes, arguments, "");
    }
}
