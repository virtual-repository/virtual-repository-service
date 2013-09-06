package org.virtualrepository.service.rest.providers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.virtualrepository.service.io.Binder;
import org.virtualrepository.service.rest.VrsMediaType;

/**
 * Intercepts all responses 
 * 
 * @author Fabio Simeoni
 *
 */
@Provider @Singleton
public class RepresentationHandler implements MessageBodyWriter<Object> {

	@Inject 
	private Binder binder;
	
	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		//applies to all responses, but those that appear already serialised, e.g. errors from barrier
		return type!=String.class;
	}
	
	@Override
	public void writeTo(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException,
			WebApplicationException {
		
		//dispatches conversion to based on media type
		
		VrsMediaType negotiated = VrsMediaType.fromMediaType(mediaType);
		
		entityStream.write(negotiated.bind(object).with(binder).getBytes());
		
	}
	
	@Override
	public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}
}
