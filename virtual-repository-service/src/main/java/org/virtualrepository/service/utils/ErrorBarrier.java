package org.virtualrepository.service.utils;

import static javax.ws.rs.core.MediaType.*;
import static javax.ws.rs.core.Response.Status.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

public class ErrorBarrier {

	@Provider
	public static class IAEMapper implements ExceptionMapper<IllegalArgumentException> {
		
		public Response toResponse(IllegalArgumentException ex) {
			return Response.status(BAD_REQUEST).entity(ex.getMessage()).type(TEXT_PLAIN).build();
		}
	}
	
	@Provider
	public static class ISEMapper implements ExceptionMapper<IllegalStateException> {
		
		public Response toResponse(IllegalStateException ex) {
			return Response.status(CONFLICT).entity(ex.getMessage()).type(TEXT_PLAIN).build();
		}
	}
}
