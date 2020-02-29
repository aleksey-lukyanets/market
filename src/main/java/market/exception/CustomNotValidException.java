package market.exception;

import org.springframework.validation.FieldError;

/**
 *
 */
public class CustomNotValidException extends Exception {
	private static final long serialVersionUID = 4461435547254498698L;

	private final String eventType;
	private final String object;
	private final String field;

	public CustomNotValidException(String eventType, String object, String field) {
		super();
		this.eventType = eventType;
		this.object = object;
		this.field = field;
	}

	public FieldError getFieldError() {
		String[] codes = {eventType + "." + object + "." + field};
		Object[] arguments = {field};
		return new FieldError(object, field, "", false, codes, arguments, "");
	}
}
