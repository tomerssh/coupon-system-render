package app.core.exceptions;

/**
 * System wide exception.
 */
public class CouponServiceException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public CouponServiceException() {
		super();
	}

	public CouponServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CouponServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouponServiceException(String message) {
		super(message);
	}

	public CouponServiceException(Throwable cause) {
		super(cause);
	}
}