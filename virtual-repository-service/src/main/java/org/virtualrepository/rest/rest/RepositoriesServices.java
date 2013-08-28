/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.rest.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.virtualrepository.impl.Services;

import com.sun.jersey.spi.resource.Singleton;

/**
 * Place your class / interface description here.
 *
 * History:
 *
 * ------------- --------------- -----------------------
 * Date			 Author			 Comment
 * ------------- --------------- -----------------------
 * 27 Aug 2013   Fiorellato     Creation.
 *
 * @version 1.0
 * @since 27 Aug 2013
 */
@Path("/repositories")
@Singleton
public class RepositoriesServices extends AbstractVirtualRepositoryServices {
	private Services doGetServices() {
		return repository().services();
	}
	
	@GET
	@Path("/meta")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJSONRepositories() {
		try {
			return this.jsonResponse(this.doGetServices());
		} catch(Throwable t) {
			return this.handleError(t);
		}
	}
	
	@GET
	@Path("/meta")
	@Produces(MediaType.APPLICATION_XML)
	public Response getXMLRepositories() {
		try {
			return this.xmlResponse(this.doGetServices());
		} catch(Throwable t) {
			return this.handleError(t);
		}
	}
}
