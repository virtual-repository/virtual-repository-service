package org.virtualrepository.service.rest.errors;

import static javax.ws.rs.core.Response.Status.*;

import javax.ws.rs.core.Response.Status;




/**
 * Known error types.
 * <p>
 * Each type can throw a corresponding {@link VrsException}.
 * 
 * @author Fabio Simeoni
 *
 */
public enum Error {

	/**
	 * The error raised when requests are made to failed applications.
	 */
	no_such_asset(NOT_FOUND,"this resource is not currently available"),
	
	
		/**
	 * The error raised when requests are made to failed applications.
	 */
	invalid_mediatype(NOT_ACCEPTABLE,"this resource is not currently available in the required type")
	
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
	
	public VrsException toException() {
		return new VrsException(this);
	}
	
	public VrsException toException(String msg) {
		return new VrsException(this, msg);
	}
	
	public VrsException toException(Throwable cause) {
		return new VrsException(this,cause);
	}
	
	public VrsException toException(String msg,Throwable cause) {
		return new VrsException(this,cause,msg);
	}
	
	
}
