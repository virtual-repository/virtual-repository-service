package org.virtualrepository.service.rest.errors;

import static javax.ws.rs.core.Response.Status.*;

import javax.ws.rs.core.Response.Status;




/**
 * Known error types.
 * <p>
 * Each type can throw a corresponding {@link ServiceException}.
 * 
 * @author Fabio Simeoni
 *
 */
public enum Error {

	/**
	 * The error raised when requests are made to failed applications.
	 */
	no_such_asset(NOT_FOUND,"this resource is not currently available")
	
	//to be continued
	
	
	;
	
	
	private final Status status;
	private final String msg;
	
	private Error(Status status,String msg) {
		this.status=status;
		this.msg=msg;
	}
	
	public Status status() {
		return status;
	}
	
	public String message() {
		return msg;
	}
	
	public void fire() {
		throw toException();
	}
	
	public void fire(String msg) {
		throw toException(msg);
	}
	
	public void fire(Throwable cause) {
		throw toException(cause);
	}
	
	public void fire(String msg,Throwable cause) {
		throw toException(msg,cause);
	}
	
	public ServiceException toException() {
		return new ServiceException(this);
	}
	
	public ServiceException toException(String msg) {
		return new ServiceException(this, msg);
	}
	
	public ServiceException toException(Throwable cause) {
		return new ServiceException(this,cause);
	}
	
	public ServiceException toException(String msg,Throwable cause) {
		return new ServiceException(this,cause,msg);
	}
	
	
}
