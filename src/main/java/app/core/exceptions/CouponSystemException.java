package app.core.exceptions;

/**
 * System wide exception.
 */
public class CouponSystemException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CouponSystemException() {
		super();
	}

	public CouponSystemException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CouponSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouponSystemException(String message) {
		super(message);
	}

	public CouponSystemException(Throwable cause) {
		super(cause);
	}
}