package org.virtualrepository.service.rest.errors;


/**
 * Thrown for the occurrence of an error during request processing.
 * 
 * @author Fabio Simeoni
 *
 */
public class ServiceException extends RuntimeException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Error error;
	
	/**
	 * Creates an instance with an underlying error.
	 * @param error the error
	 */
	public ServiceException(Error error) {
		this(error, error.message());
	}
	
	/**
	 * Creates an instance with an underling error and a custom message.
	 * @param message the message
	 * @param error the error
	 */
	public ServiceException(Error error,String message) {
		super(message);
		this.error=error;
	}
	
	/**
	 * Creates an instance with an underlying error and an underlying cause
	 * @param error the error
	 * @param cause the cause;
	 */
	public ServiceException(Error error,Throwable cause) {
		this(error, cause, error.message());
	}
	
	/**
	 * Creates an instance with an underlying error, an underlying cause, and an underlying message.
	 * @param error the error
	 * @param cause the cause;
	 * @Param message the message;
	 */
	public ServiceException(Error error,Throwable cause,String message) {
		super(message,cause);
		this.error=error;
	}
	
	/**
	 * Returns the underlying error.
	 * @return the error
	 */
	public Error error() {
		return error;
	}
	
}
