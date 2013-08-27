/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.service.exceptions;

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
public class ServiceConfigurationException extends ServiceException {
	/** Field serialVersionUID */
	private static final long serialVersionUID = 7422423060915700551L;

	/**
	 * Class constructor
	 *
	 */
	public ServiceConfigurationException() {
		super();
	}

	/**
	 * Class constructor
	 *
	 * @param message
	 * @param cause
	 */
	public ServiceConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Class constructor
	 *
	 * @param message
	 */
	public ServiceConfigurationException(String message) {
		super(message);
	}

	/**
	 * Class constructor
	 *
	 * @param cause
	 */
	public ServiceConfigurationException(Throwable cause) {
		super(cause);
	}
}