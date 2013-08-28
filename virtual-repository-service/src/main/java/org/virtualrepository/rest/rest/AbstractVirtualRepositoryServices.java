/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.rest.rest;

import static org.dynamicvalues.Directives.all;
import static org.dynamicvalues.Directives.by;
import static org.dynamicvalues.Directives.type;

import java.util.Collection;
import java.util.HashSet;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.dynamicvalues.Dynamic;
import org.virtualrepository.AssetType;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.ServiceProxy;

import com.thoughtworks.xstream.XStream;

import flexjson.JSONSerializer;

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
abstract public class AbstractVirtualRepositoryServices {
	static private VirtualRepository REPO;
	
	static private String[] JSON_DEFAULT_EXCLUSION_PATTERNS = { "class", "*.class" };
	static private XStream XSTREAM;
	static {
		REPO = new Repository();
		REPO.discover(CsvCodelist.type, SdmxCodelist.type);
		
		XSTREAM = new XStream();
		XSTREAM.omitField(RepositoryService.class, "proxy");
		XSTREAM.setMode(XStream.ID_REFERENCES);
	}
	
	protected VirtualRepository repository() {
		return REPO;
	}

	protected AssetType[] availableTypes() {
		Collection<AssetType> availableTypes = new HashSet<AssetType>();
		
		for(RepositoryService service : REPO.services()) {
			for(AssetType type : service.returnedTypes())
				availableTypes.add(type);
		}
		
		return availableTypes.toArray(new AssetType[0]);
	}
	
	protected String jsonify(Object object) throws Exception {
		return this.jsonify(object, (String[])null);
	}

	protected String jsonify(Object object, String... exclusionPatterns) throws Exception {
		return new JSONSerializer().
				   exclude(JSON_DEFAULT_EXCLUSION_PATTERNS).
				   exclude(exclusionPatterns == null ? new String[0] : exclusionPatterns).
				   deepSerialize(Dynamic.valueOf(object, 
						   						 by().excluding(all(type(ServiceProxy.class)))));
	}
	
	protected Response jsonResponse(Object object) throws Exception {
		return this.jsonResponse(object, (String[])null);
	}
	
	protected Response jsonResponse(Object object, String... exclusionPatterns) throws Exception {
		return Response.status(Response.Status.OK).entity(this.jsonify(object, exclusionPatterns)).build();
	}
	
	protected Response xmlResponse(Object object, String... exclusionPatterns) throws Exception {
		return Response.status(Response.Status.OK).entity(XSTREAM.toXML(object)).build();
	}
	
	protected Response handleError(Throwable t) {
		return this.status(Response.Status.INTERNAL_SERVER_ERROR, t.getMessage());
	}
	
	protected Response badRequest() {
		return this.badRequest(null);
	}
	
	protected Response badRequest(String message) {
		return this.status(Response.Status.BAD_REQUEST, message);
	}
	
	protected Response notFound() {
		return this.notFound(null);
	}
	
	protected Response notFound(String message) {
		return this.status(Response.Status.NOT_FOUND, message);
	}
	
	protected Response error() {
		return this.error(null);
	}
	
	protected Response error(String message) {
		return this.status(Response.Status.INTERNAL_SERVER_ERROR, message);
	}
	
	protected Response status(Response.Status code) {
		return this.status(code, null);
	}
	
	protected Response status(Response.Status code, String message) {
		ResponseBuilder builder = Response.status(code);
		
		if(message != null)
			builder.entity(message);
		
		return builder.build();
	}
}