package market.dto.exception;

/**
 * Нарушение ограничения поля при валидации.
 */
public class FieldErrorDTO {

	private final String field;
	private final String message;

	public FieldErrorDTO(String field, String message) {
		this.field = field;
		this.message = message;
	}

	public String getField() {
		return field;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "{" +
			"field='" + field + '\'' +
			", message='" + message + '\'' +
			'}';
	}
}
