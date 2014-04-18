package market.exception;

import org.springframework.validation.FieldError;

/**
 * 
 */
public class EmailExistsException extends Exception {
    
    public EmailExistsException() {
        super("");
    }

    public EmailExistsException(String message) {
        super(message);
    }
    
    public FieldError getFieldError() {
        String[] codes = {"Exists.userDTO.email"};
        Object[] arguments = {"email"};
        return new FieldError("userDTO", "email", "", false, codes, arguments, "");
    }
}
