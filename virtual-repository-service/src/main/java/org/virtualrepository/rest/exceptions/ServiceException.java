/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.rest.exceptions;

/**
 * Place your class / interface description here.
 *
 * History:
 *
 * ------------- --------------- -----------------------
 * Date			 Author			 Comment
 * ------------- --------------- -----------------------
 * 27 Aug 2013   Fiorellato     Creation.
 *
 * @version 1.0
 * @since 27 Aug 2013
 */
public class ServiceException extends Exception {
	/** Field serialVersionUID */
	private static final long serialVersionUID = -9161329732529204514L;

	/**
	 * Class constructor
	 *
	 */
	public ServiceException() {
		super();
	}

	/**
	 * Class constructor
	 *
	 * @param message
	 * @param cause
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Class constructor
	 *
	 * @param message
	 */
	public ServiceException(String message) {
		super(message);
	}

	/**
	 * Class constructor
	 *
	 * @param cause
	 */
	public ServiceException(Throwable cause) {
		super(cause);
	}
}
