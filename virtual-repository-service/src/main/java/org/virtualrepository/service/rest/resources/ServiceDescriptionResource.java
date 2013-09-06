/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.service.rest.resources;

import static org.virtualrepository.service.Constants.*;
import static org.virtualrepository.service.rest.resources.ServiceDescriptionResource.*;

import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.virtualrepository.service.configuration.Configuration;


/**
 * Describes the endpoint.
 * 
 * @author Fabio Fiorellato
 * @author Fabio Simeoni
 *
 */
@Path(path)
@Singleton
public class ServiceDescriptionResource {
	
	public static final String path = "/";
	
	private final Configuration configuration;
	

	@Inject
	public ServiceDescriptionResource(Configuration configuration) {
	
		this.configuration=configuration;
	}
	
	
	@GET
	@Produces({jmom,xmom})
	public Properties describe() {
		
		return configuration.properties();

	}
}