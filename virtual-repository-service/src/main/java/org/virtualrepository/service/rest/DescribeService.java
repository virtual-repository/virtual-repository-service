/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.service.rest;

import static javax.ws.rs.core.MediaType.*;
import static javax.ws.rs.core.Response.*;
import static org.virtualrepository.service.rest.DescribeService.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.virtualrepository.service.configuration.Configuration;
import org.virtualrepository.service.io.Binder;


/**
 * Describes the endpoint.
 * 
 * @author Fabio Fiorellato
 * @author Fabio Simeoni
 *
 */
@Path(path)
@Singleton
public class DescribeService {
	
	public static final String path = "/";
	
	private final Binder binder;
	private final Configuration configuration;
	

	@Inject
	public DescribeService(Configuration configuration,Binder binder) {
	
		this.configuration=configuration;
		this.binder=binder;
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response describeInJson() {
		
		return ok(binder.jsonMoM(configuration.properties())).type(APPLICATION_JSON).build();

	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response describeInXml() {
		
		return ok(binder.xmlMoM(configuration.properties())).type(APPLICATION_XML).build();
	}
}