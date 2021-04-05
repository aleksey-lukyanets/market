package market.rest.exception;

import market.dto.exception.FieldErrorDTO;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Generalized response for the case of a request processing error.
 */
public class RestErrorResponse {

	private final String message;
	private final String description;
	private final String entityName;
	private final List<FieldErrorDTO> fieldErrors = new ArrayList<>();

	public RestErrorResponse(String message, WebRequest request) {
		this(message, null, request.getDescription(false));
	}

	public RestErrorResponse(String message, String entityName, WebRequest request) {
		this(message, entityName, request.getDescription(false));
	}

	public RestErrorResponse(String message, String entityName, String description) {
		this.message = message;
		this.entityName = entityName;
		this.description = description;
	}

	public void addFieldError(String path, String message) {
		fieldErrors.add(new FieldErrorDTO(path, message));
	}

	public String getDescription() {
		return description;
	}

	public String getMessage() {
		return message;
	}

	public String getEntityName() {
		return entityName;
	}

	public List<FieldErrorDTO> getFieldErrors() {
		return fieldErrors;
	}

	@Override
	public String toString() {
		return "RestErrorResponse{" +
			"message='" + message + '\'' +
			", entityName='" + entityName + '\'' +
			", description='" + description + '\'' +
			", fieldErrors=" + fieldErrors +
			'}';
	}
}
