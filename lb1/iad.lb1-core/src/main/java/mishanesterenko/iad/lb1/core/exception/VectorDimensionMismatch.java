package mishanesterenko.iad.lb1.core.exception;

public class VectorDimensionMismatch extends Exception {

	/**
	 * Generated serial uid.
	 */
	private static final long serialVersionUID = 5375814446007165141L;

	public VectorDimensionMismatch() {
	}

	public VectorDimensionMismatch(String message) {
		super(message);
	}

	public VectorDimensionMismatch(Throwable cause) {
		super(cause);
	}

	public VectorDimensionMismatch(String message, Throwable cause) {
		super(message, cause);
	}

}
