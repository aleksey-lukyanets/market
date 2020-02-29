package market.exception;

/**
 * Попытка сослаться на неизвестный товар.
 */
public class UnknownEntityException extends CustomNotValidException {
	private static final long serialVersionUID = 4827971686664741607L;

	private final Class<?> clazz;
	private final long entityId;

	public UnknownEntityException(Class<?> clazz, long entityId) {
		super("NotExist", clazz.getSimpleName(), "id");
		this.clazz = clazz;
		this.entityId = entityId;
	}

	public String getEntityType() {
		return clazz.getSimpleName();
	}

	public long getEntityId() {
		return entityId;
	}
}
