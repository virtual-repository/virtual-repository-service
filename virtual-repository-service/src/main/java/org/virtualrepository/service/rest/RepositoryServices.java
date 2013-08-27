/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.virtualrepository.RepositoryService;

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
@Path("/repository")
@Singleton
public class RepositoryServices extends AbstractVirtualRepositoryServices {
	private RepositoryService doGetRepository(String id) {
		for(RepositoryService service : repository().services())
			if(service.name().getLocalPart().equals(id))
				return service;
		
		return null;
	}
	
	@GET
	@Path("/meta/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJSONRepository(@PathParam("id") String id) {
		try {
			RepositoryService repo = this.doGetRepository(id);
			
			if(repo == null)
				return this.notFound("Unknown repository '" + id + "'");
			
			return this.jsonResponse(repo);
		} catch(Throwable t) {
			return this.handleError(t);
		}
	}
	
	@GET
	@Path("/meta/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getXMLRepository(@PathParam("id") String id) {
		try {
			RepositoryService repo = this.doGetRepository(id);
			
			if(repo == null)
				return this.notFound("Unknown repository '" + id + "'");
			
			return this.xmlResponse(repo);
		} catch(Throwable t) {
			return this.handleError(t);
		}
	}
}
