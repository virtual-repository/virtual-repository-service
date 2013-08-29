package org.virtualrepository.service.utils;

/**
 * App-wide utilities.
 * 
 * @author Fabio Simeoni
 *
 */
public class Utils {


	public static RuntimeException unchecked(Throwable t) {

		return unchecked(t.getMessage()+"( unchecked wrapper )",t);

	}
	
	public static RuntimeException unchecked(String msg, Throwable t) {

		return (t instanceof RuntimeException) ? RuntimeException.class.cast(t) : new RuntimeException(msg,
				t);

	}
	
	public static RuntimeException wrapped(String msg, Throwable t) {

		return new RuntimeException(msg,t);

	}

	public static void rethrow(String msg,Throwable t) throws RuntimeException {
		
		throw wrapped(msg,t);

	}
	
	public static void rethrowUnchecked(String msg,Throwable t) throws RuntimeException {

		throw unchecked(msg,t);

	}
	
	public static void rethrowUnchecked(Throwable t) throws RuntimeException {

		throw unchecked(t);

	}
}
