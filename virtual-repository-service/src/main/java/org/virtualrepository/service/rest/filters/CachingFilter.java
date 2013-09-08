package org.virtualrepository.service.rest.filters;

import static javax.ws.rs.core.HttpHeaders.*;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.virtualrepository.service.configuration.Configuration;
import org.virtualrepository.service.rest.resources.Cacheable;

import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

/**
 * Intercepts responses to set cache-related HTTP headers based on explicit {@link Fake} or defaults.
 * 
 * @author Fabio Simeoni
 *
 */
public class CachingFilter implements ResourceFilter, ContainerRequestFilter, ContainerResponseFilter {

	
	private final CacheControl directives;
	private final AbstractMethod method;
	private final ResourceContext context;

	public CachingFilter(ResourceContext context, AbstractMethod method, Configuration configuration) {
		
		this.context=context;
		this.method=method;
		
		CacheDirectives cache = method.getAnnotation(CacheDirectives.class);
		this.directives = cache==null ? defaultDirectives(configuration):
										directivesFrom(cache, configuration);
		
	}
	
	@Override
	public ContainerRequest filter(ContainerRequest request) {
		
		if (isCacheable()) { //add validators
			
			Object resource = context.getResource(method.getResource().getResourceClass());
			
			if (resource instanceof Cacheable) {
				
				Cacheable cacheable = Cacheable.class.cast(resource);
				
				Response nochange = prepareNoChangeFor(request, cacheable);
				
				if (nochange!=null)
					//weird but works: we do not actually communicate an error here but bypass resource and go
					//straight to response filter
					throw new WebApplicationException(nochange);
				
			}
		}
		return request;
		
	}

	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {

		//dont act on errors or in the lack of directives (e.g. on POST)
		if (!isError(response) && isCacheable()) {
		
			ResponseBuilder builder = Response.fromResponse(response.getResponse());
			
			//add validators if cacheable
			Object resource = context.getResource(method.getResource().getResourceClass());
			
			if (resource instanceof Cacheable) {
			
				Cacheable cacheable = Cacheable.class.cast(resource);
				
				builder.lastModified(cacheable.lastModified().getTime()).tag(cacheable.etag());
				
			}
			
			//directives for caches
			builder.cacheControl(directives);
			
			//more directives: consider Accept header as part of cache entry 
			builder.header(VARY,ACCEPT);
			
			response.setResponse(builder.build());
			
		
		}
		
		return response;
		
	}
	

	@Override
	public ContainerRequestFilter getRequestFilter() {
		return this;
	}

	@Override
	public ContainerResponseFilter getResponseFilter() {
		return this;
	}
	
	
	//helpers
	private CacheControl directivesFrom(CacheDirectives annotation,Configuration configuration) {
		
		CacheControl directives = new CacheControl();

		if (annotation.max_age() >= 0)
			directives.setMaxAge(annotation.max_age());
		
		directives.setNoCache(annotation.no_cache());
		
		return directives;
	}
	
	private CacheControl defaultDirectives(Configuration configuration) {
		
		if (!method.isAnnotationPresent(GET.class))
			return null;
		
		CacheControl directives = new CacheControl();
		directives.setMaxAge(configuration.responseTTL());
		
		return directives;
	}
	
	private boolean isCacheable() {
		return directives!=null; 
	}
	
	private boolean isError(ContainerResponse response) {
		return response.getStatus()>=400; //can we do better than this?
	}
	
	private Response prepareNoChangeFor(ContainerRequest request, Cacheable resource) {

		EntityTag etag=null;
		Date lastModified = null;
		
		if (resource.etag()!=null)
			etag = new EntityTag(resource.etag());
		
		if (resource.lastModified()!=null)
			lastModified = resource.lastModified().getTime();
		
		
		ResponseBuilder builder = null;
		if (etag!=null && lastModified!=null)
			builder = request.evaluatePreconditions(lastModified,etag);
		else
			if (etag!=null)
				builder = request.evaluatePreconditions(etag);
			else
				builder = request.evaluatePreconditions(lastModified);
			
		return builder == null ? null : builder.build();
	}
	
}
