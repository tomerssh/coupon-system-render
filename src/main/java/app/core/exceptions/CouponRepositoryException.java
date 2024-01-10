package app.core.exceptions;

/**
 * System wide exception.
 */
public class CouponRepositoryException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public CouponRepositoryException() {
		super();
	}

	public CouponRepositoryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CouponRepositoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouponRepositoryException(String message) {
		super(message);
	}

	public CouponRepositoryException(Throwable cause) {
		super(cause);
	}
}