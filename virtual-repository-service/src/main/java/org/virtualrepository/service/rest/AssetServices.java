/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.Asset;
import org.virtualrepository.service.utils.ManagementUtilities;
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
	
	private Object doRetrieveAsset(String id, String model) {
		Object data = repository().retrieve(repository().lookup(id), ManagementUtilities.apiForName(model == null ? ModelConstants.TABLE : model));
				
		if(data == null)
			return null;
		
		Class<?> clazz = data.getClass();
		
		if(Table.class.isAssignableFrom(clazz)) {
			Table asTable = (Table)data;
			
			if(!asTable.iterator().hasNext())
				return null;
		}
		
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
	@Path("/meta/{id}")
	@Produces(RequestConstants.APPLICATION_VXML)
	public Response getVXMLAssetMetadata(@PathParam("id") String id) {
		try {
			return this.vxmlResponse(doLookupAsset(id));
		} catch (IllegalStateException ISe) {
			return this.notFound(ISe.getMessage());
		} catch (Throwable t) {
			return this.handleError(t);
		}
	}
	
	@GET
	@Path("/retrieve/{id}")
	public Response retrieveAssetData(@HeaderParam("Accept") String acceptHeader, @PathParam("id") String id, @QueryParam("model") String model) {
		try {
			Object data = this.doRetrieveAsset(id, model);
			
			if(data == null)
				return this.status(Status.NO_CONTENT);

			String mediaType = ManagementUtilities.getProperMediaTypeFor(model, acceptHeader);

			if(RequestConstants.APPLICATION_VXML.equals(mediaType))
				return this.vxmlResponse(data);
			else if(MediaType.APPLICATION_JSON.equals(mediaType))
				return this.jsonResponse(data);
			else if(MediaType.APPLICATION_XML.equals(mediaType))
				return this.xmlResponse(data);
			else {
				if(CodelistBean.class.isAssignableFrom(data.getClass())) {
					//CodelistBean asCodelistBean = (CodelistBean)data;
					
					throw new RuntimeException("Not yet supported... :(");
				}
			}
			
			//Default (this sucks)
			return this.xmlResponse(data);
		} catch (IllegalStateException ISe) {
			return this.notFound(ISe.getMessage());
		} catch (IllegalArgumentException IAe) {
			return this.badRequest(IAe.getMessage());
		} catch (Throwable t) {
			return this.handleError(t);
		}
	}
}
