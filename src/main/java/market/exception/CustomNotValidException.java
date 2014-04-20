package market.exception;

import org.springframework.validation.FieldError;

/**
 * 
 */
public class CustomNotValidException extends Exception {
    
    private final String type;
    private final String object;
    private final String field;

    public CustomNotValidException(String type, String object, String field) {
        super();
        this.type = type;
        this.object = object;
        this.field = field;
    }
    
    public FieldError getFieldError() {
        String[] codes = {type + "." + object + "." + field};
        Object[] arguments = {field};
        return new FieldError(object, field, "", false, codes, arguments, "");
    }
}
