package market.exception;

import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public class CustomNotValidException extends RuntimeException {
	private static final long serialVersionUID = 4461435547254498698L;

	private final String eventType;
	private final String entityType;
	private final String field;

	public CustomNotValidException(String eventType, String entityType, String field) {
		super();
		this.eventType = eventType;
		this.entityType = entityType;
		this.field = field;
	}

	public FieldError getFieldError() {
		String[] codes = {eventType + "." + field, eventType + "." + entityType + "." + field};
		Object[] arguments = {field};
		return new FieldError(entityType, field, "", false, codes, arguments, "");
	}

	public List<FieldError> getFieldErrors() {
		return Collections.singletonList(getFieldError());
	}

	public String getEntityType() {
		return entityType;
	}
}
