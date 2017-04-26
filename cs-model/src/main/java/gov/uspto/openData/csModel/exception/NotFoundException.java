package gov.uspto.openData.csModel.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NotFoundException(){
		
	}

	/**
	 * Constructor with custom message and cause.
	 * 
	 * @param customMessage
	 *             Exception message.
	 */
	public NotFoundException(String message) {
		super(message);
	}

	/**
	 * Constructor with custom message and cause.
	 * 
	 * @param customMessage
	 *            Exception message.
	 * @param cause
	 *            Exception cause.
	 */
	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
