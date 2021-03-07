package market.rest.exception;

import market.exception.CustomNotValidException;
import market.exception.UnknownEntityException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Locale;

@ControllerAdvice(basePackages = "market.rest")
@RestController
public class RestExceptionHandler {
	private static final String ARGUMENT_VALIDATION_CODE = "Error.Validation.Parameter";
	private static final String NOT_EXIST_CODE = "NotExist";

	private static final Logger log = LogManager.getLogger(RestExceptionHandler.class);

	private final MessageSource messageSource;

	public RestExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestErrorResponse otherExceptions(Exception e, WebRequest request) {
		RestErrorResponse response = new RestErrorResponse(e.getMessage(), request);
		log.error(response.toString(), e);
		return response;
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public RestErrorResponse accessDeniedException(AccessDeniedException e, WebRequest request) {
		RestErrorResponse response = new RestErrorResponse(e.getMessage(), request);
		log.warn(response.toString());
		return response;
	}

	@ExceptionHandler(UnknownEntityException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public RestErrorResponse unknownEntityException(UnknownEntityException e, WebRequest request) {
		String entityType = e.getEntityType();
		List<FieldError> fieldErrors = e.getFieldErrors();
		RestErrorResponse response = createLocalizedResponse(NOT_EXIST_CODE, entityType, fieldErrors, request);
		log.warn(response.toString());
		return response;
	}

	@ExceptionHandler(CustomNotValidException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public RestErrorResponse customNotValidException(CustomNotValidException e, WebRequest request) {
		String entityType = e.getEntityType();
		List<FieldError> fieldErrors = e.getFieldErrors();
		RestErrorResponse response = createLocalizedResponse(ARGUMENT_VALIDATION_CODE, entityType, fieldErrors, request);
		log.warn(response.toString());
		return response;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public RestErrorResponse processValidationError(MethodArgumentNotValidException e, WebRequest request) {
		BindingResult result = e.getBindingResult();
		String objectName = result.getObjectName();
		List<FieldError> fieldErrors = result.getFieldErrors();
		RestErrorResponse response = createLocalizedResponse(ARGUMENT_VALIDATION_CODE, objectName, fieldErrors, request);
		log.warn(response.toString());
		return response;
	}

	private RestErrorResponse createLocalizedResponse(String localizationCode, String entityType,
		List<FieldError> fieldErrors, WebRequest request)
	{
		Locale currentLocale = LocaleContextHolder.getLocale();
		String localizedMessage = messageSource.getMessage(localizationCode, null, currentLocale);
		RestErrorResponse response = new RestErrorResponse(localizedMessage, entityType, request);
		return resolveFieldErrors(response, fieldErrors);
	}

	private RestErrorResponse resolveFieldErrors(RestErrorResponse response, List<FieldError> fieldErrors) {
		for (FieldError fieldError : fieldErrors) {
			String localizedErrorMessage = resolveErrorMessage(fieldError);
			response.addFieldError(fieldError.getField(), localizedErrorMessage);
		}
		return response;
	}

	private String resolveErrorMessage(FieldError fieldError) {
		Locale currentLocale = LocaleContextHolder.getLocale();
		String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);

		if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
			String[] fieldErrorCodes = fieldError.getCodes();
			if (fieldErrorCodes != null)
				localizedErrorMessage = fieldErrorCodes[0];
		}
		return localizedErrorMessage;
	}
}
