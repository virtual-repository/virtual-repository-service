/**
 * (c) 2013 FAO / UN (project: virtual-produced-service)
 */
package org.virtualrepository.service.rest.resources;

import static org.virtualrepository.service.Constants.*;
import static org.virtualrepository.service.rest.resources.Description.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.virtualrepository.AssetType;
import org.virtualrepository.impl.Services;
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
public class Description {
	
	public static final String path = "/";
	
	private final Configuration configuration;
	

	@Inject
	public Description(Configuration configuration) {
	
		this.configuration=configuration;
	}
	
	
	@GET
	@Produces({jmom,xmom})
	public Object describe() {
		
		final String n = configuration.name();
		final String v = configuration.version();
		final AssetType[] ts = configuration.assetTypes();
		final Services ss = configuration.repositories();
		
		
		@SuppressWarnings("all")
		Object description = new Object() {
			String name = n;
			String version = v;
			AssetType[] types = ts;
			Services repositories = ss;
			
		};
		
		return description;

	}
}