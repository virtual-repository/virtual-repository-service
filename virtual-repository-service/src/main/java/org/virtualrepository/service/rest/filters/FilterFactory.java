package org.virtualrepository.service.rest.filters;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Context;

import org.virtualrepository.service.configuration.Configuration;

import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

/**
 * Registers filters on resource methods
 * @author Fabio Simeoni
 *
 */
@Singleton
public class FilterFactory implements ResourceFilterFactory {

	//since we create filters, we are responsible for injecting their dependencies
	//we get them injected and pass them on
	private final Configuration config;
	private final ResourceContext context;
	
	@Inject //configuration comes from CDI and context comes from Jersey
	public FilterFactory(@Context ResourceContext context,Configuration config) {
		this.context=context;
		this.config=config;
	}
	
	@Override
	public List<ResourceFilter> create(AbstractMethod method) {

		List<ResourceFilter> filters = new ArrayList<ResourceFilter>();

		//cache-related headers management
		filters.add(new CachingFilter(context,method,config));

		//add more filters here if need arises
		
		return filters;
	}

}
