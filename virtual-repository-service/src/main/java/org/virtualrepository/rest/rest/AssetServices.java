/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.rest.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.virtualrepository.Asset;
import org.virtualrepository.tabular.Table;

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
@Path("/asset")
@Singleton
public class AssetServices extends AbstractVirtualRepositoryServices {
	private Asset doLookupAsset(String id) {
		return repository().lookup(id);
	}
	
	private Table doRetrieveAsset(String id) {
		Table data = repository().retrieve(repository().lookup(id), Table.class);
		
		if(data == null || !data.iterator().hasNext())
			return null;
		
		return data;
	}
	
	@GET
	@Path("/meta/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJSONAssetMetadata(@PathParam("id") String id) {
		try {
			return this.jsonResponse(doLookupAsset(id));
		} catch (IllegalStateException ISe) {
			return this.notFound(ISe.getMessage());
		} catch (Throwable t) {
			return this.handleError(t);
		}
	}
	
	@GET
	@Path("/meta/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getXMLAssetMetadata(@PathParam("id") String id) {
		try {
			return this.xmlResponse(doLookupAsset(id));
		} catch (IllegalStateException ISe) {
			return this.notFound(ISe.getMessage());
		} catch (Throwable t) {
			return this.handleError(t);
		}
	}
	
	@GET
	@Path("/retrieve/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveJSONAssetData(@PathParam("id") String id) {
		try {
			Table data = this.doRetrieveAsset(id);
			
			if(data == null || !data.iterator().hasNext())
				return this.status(Status.NO_CONTENT);
			
			return this.jsonResponse(data);
		} catch (IllegalStateException ISe) {
			return this.notFound(ISe.getMessage());
		} catch (Throwable t) {
			return this.handleError(t);
		}
	}
	
	@GET
	@Path("/retrieve/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response retrieveXMLAssetData(@PathParam("id") String id) {
		try {
			Table data = this.doRetrieveAsset(id);
			
			if(data == null || !data.iterator().hasNext())
				return this.status(Status.NO_CONTENT);
			
			return this.xmlResponse(data);
		} catch (IllegalStateException ISe) {
			return this.notFound(ISe.getMessage());
		} catch (Throwable t) {
			return this.handleError(t);
		}
	}
}
