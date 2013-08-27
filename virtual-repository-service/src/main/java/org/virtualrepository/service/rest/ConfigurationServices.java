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
import org.virtualrepository.service.model.ConfigurationData;

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
	private ConfigurationData _config;
	
	public ConfigurationServices() throws IOException {
		super();
		
		Properties conf = new Properties();
		
		conf.load(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("configuration.properties"), Charset.forName("UTF-8")));
		
		Collection<String> assetTypeNames = new HashSet<String>();
		
		for(AssetType type : availableTypes())
			assetTypeNames.add(type.name());
		
		this._config = new ConfigurationData(conf.getProperty("vrs.local.name"),
											 conf.getProperty("vrs.shared.vr.version"),
											 assetTypeNames.toArray(new String[0]));
	}
	
	@GET
	@Path("/info")
	@Produces(MediaType.APPLICATION_JSON)
	public Response info() {
		try {
			return this.jsonResponse(this._config);
		} catch (Throwable t) {
			return this.handleError(t);
		}
	}
}