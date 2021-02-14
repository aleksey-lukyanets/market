package market.exception;

/**
 * Попытка сослаться на неизвестный товар.
 */
public class UnknownEntityException extends CustomNotValidException {
	private static final long serialVersionUID = 4827971686664741607L;

	private final String idField;
	private final long idValue;

	public UnknownEntityException(Class<?> clazz, long idValue) {
		this(clazz, "id", idValue);
	}

	public UnknownEntityException(Class<?> clazz, String idField, long idValue) {
		super("NotExist", clazz.getSimpleName(), idField);
		this.idField = idField;
		this.idValue = idValue;
	}

	public String getIdField() {
		return idField;
	}

	public long getIdValue() {
		return idValue;
	}
}
