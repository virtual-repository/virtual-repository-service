/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.service.rest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.virtualrepository.AssetType;

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
@Path("/configuration")
@Singleton
public class ConfigurationServices extends AbstractVirtualRepositoryServices {
	private Properties _config;
	
	public ConfigurationServices() throws IOException {
		super();
		
		this._config = new Properties();
		
		this._config.load(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("configuration.properties"), Charset.forName("UTF-8")));
		
		Collection<String> assetTypeNames = new HashSet<String>();
		
		for(AssetType type : availableTypes())
			assetTypeNames.add(type.name());
		
		this._config.put("vrs.available.types", assetTypeNames.toArray(new String[0]));
	}
	
	@GET
	@Path("/info")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJSONInfo() {
		try {
			return this.jsonResponse(this._config);
		} catch (Throwable t) {
			return this.handleError(t);
		}
	}
	
	@GET
	@Path("/info")
	@Produces(MediaType.APPLICATION_XML)
	public Response getXMLInfo() {
		try {
			return this.xmlResponse(this._config);
		} catch (Throwable t) {
			return this.handleError(t);
		}
	}
}