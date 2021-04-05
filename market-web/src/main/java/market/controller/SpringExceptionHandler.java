package market.controller;

import market.exception.CustomNotValidException;
import market.exception.EmailExistsException;
import market.exception.EmptyCartException;
import market.exception.UnknownEntityException;
import market.dto.exception.ValidationErrorDTO;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Locale;

/**
 * Обработчик исключений.
 */
@Controller
@ControllerAdvice(basePackages = {"market.controller"})
public class SpringExceptionHandler {
	private final MessageSource messageSource;

	public SpringExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	//-------------------------------------------------- Обработчики исключений

	/**
	 * Запрос пользователем несуществующих объектов.
	 */
	@ExceptionHandler(UnknownEntityException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public String handleProductNotFoundException(Exception ex) {
		return ex.getMessage();
	}

	/**
	 * Исключения при обработке переданных пользователем объектов.
	 * Ответ сервера сопровождается пояснениями.
	 *
	 * @return перечень нарушенных ограничений
	 */
	@ExceptionHandler({EmailExistsException.class, EmptyCartException.class})
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ResponseBody
	public ValidationErrorDTO handleEmailExistsException(CustomNotValidException ex) {
		return processFieldErrors(ex.getFieldErrors());
	}

	/**
	 * Ошибки валидации полученного от клиента объекта.
	 * Ответ сервера сопровождается пояснениями.
	 *
	 * @return перечень нарушенных ограничений
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ResponseBody
	public ValidationErrorDTO processValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		return processFieldErrors(fieldErrors);
	}

	//----------------------------------------- Компоновка сообщений об ошибках

	private ValidationErrorDTO processFieldErrors(List<FieldError> fieldErrors) {
		ValidationErrorDTO dto = new ValidationErrorDTO();
		for (FieldError fieldError : fieldErrors) {
			String localizedErrorMessage = resolveErrorMessage(fieldError);
			dto.addFieldError(fieldError.getField(), localizedErrorMessage);
		}
		return dto;
	}

	private String resolveErrorMessage(FieldError fieldError) {
		Locale currentLocale = LocaleContextHolder.getLocale();
		String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);

		// Если подходящего сообщения не найдено - попытаться найти ближайшее по коду ошибки
		if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
			String[] fieldErrorCodes = fieldError.getCodes();
			localizedErrorMessage = fieldErrorCodes[0];
		}
		return localizedErrorMessage;
	}
}
