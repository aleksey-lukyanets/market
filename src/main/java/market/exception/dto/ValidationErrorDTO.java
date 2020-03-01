package market.exception.dto;

import java.util.*;

/**
 * Список нашенных при валидации ограничений.
 */
public class ValidationErrorDTO {

	private final List<FieldErrorDTO> fieldErrors = new ArrayList<>();

	public void addFieldError(String path, String message) {
		FieldErrorDTO error = new FieldErrorDTO(path, message);
		fieldErrors.add(error);
	}

	public List<FieldErrorDTO> getFieldErrors() {
		return fieldErrors;
	}
}
