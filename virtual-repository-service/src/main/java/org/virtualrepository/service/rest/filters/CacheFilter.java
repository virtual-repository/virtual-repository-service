package org.virtualrepository.service.rest.filters;

import javax.ws.rs.GET;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.virtualrepository.service.configuration.Configuration;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

/**
 * Intercepts responses to set cache-related HTTP headers based on explicit {@link CacheDirectives} or defaults.
 * 
 * @author Fabio Simeoni
 *
 */
public class CacheFilter implements ResourceFilter, ContainerResponseFilter {

	
	private final CacheControl directives;
	private final AbstractMethod method;

	public CacheFilter(AbstractMethod method, Configuration configuration) {
		

		this.method=method;
		
		CacheDirectives cache = method.getAnnotation(CacheDirectives.class);
		this.directives = cache==null ? defaultDirectives(configuration):
										directivesFrom(cache, configuration);
		
	}

	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {

		if (response.getStatus()<400) {
		
			if (directives!=null) {
				
				ResponseBuilder r = Response.fromResponse(response.getResponse());
		
				Response newResponse = r.cacheControl(directives).build();
		
				response.setResponse(newResponse);
			}
		}
		
		return response;
		
	}

	@Override
	public ContainerRequestFilter getRequestFilter() {
		return null;
	}

	@Override
	public ContainerResponseFilter getResponseFilter() {
		return this;
	}
	
	
	//helpers
	public CacheControl directivesFrom(CacheDirectives annotation,Configuration configuration) {
		
		CacheControl directives = new CacheControl();

		if (annotation.max_age() >= 0)
			directives.setMaxAge(annotation.max_age());
		
		directives.setNoCache(annotation.no_cache());
		
		return directives;
	}
	
	public CacheControl defaultDirectives(Configuration configuration) {
		
		if (!method.isAnnotationPresent(GET.class))
			return null;
		
		CacheControl directives = new CacheControl();
		directives.setMaxAge(configuration.responseTTL());
		
		return directives;
	}
	
}
