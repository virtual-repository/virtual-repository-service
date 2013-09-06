package org.virtualrepository.service.rest.filters;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.virtualrepository.service.configuration.Configuration;

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

	@Inject Configuration config;
	
	@Override
	public List<ResourceFilter> create(AbstractMethod method) {

		List<ResourceFilter> filters = new ArrayList<ResourceFilter>();

		//cache-related headers management
		filters.add(new CacheFilter(method,config));

		//add more filters here if need arises
		
		return filters;
	}

}
