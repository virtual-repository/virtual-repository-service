package org.virtualrepository.service.rest.providers;

import static javax.ws.rs.core.MediaType.*;
import static javax.ws.rs.core.Response.Status.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.virtualrepository.service.rest.errors.VrsException;

public class ErrorBarrier {

	@Provider
	public static class ServiceExceptionMapper implements ExceptionMapper<VrsException> {
		
		public Response toResponse(VrsException e) {
			return response(e.error().status(),e);
		}
	}
	
	@Provider
	public static class IAEMapper implements ExceptionMapper<IllegalArgumentException> {
		
		public Response toResponse(IllegalArgumentException e) {
			return response(BAD_REQUEST,e);
		}
	}
	
	@Provider
	public static class ISEMapper implements ExceptionMapper<IllegalStateException> {
		
		public Response toResponse(IllegalStateException e) {
			return response(CONFLICT,e);
		}
	}
	
	
	private static Response response(Status status, Exception e) {
		return Response.status(status).entity(e.getMessage()).type(TEXT_PLAIN).build();
		
	}
	
}
