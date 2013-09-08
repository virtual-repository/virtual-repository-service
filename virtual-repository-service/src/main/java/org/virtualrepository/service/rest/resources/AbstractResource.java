/**
 * (c) 2013 FAO / UN (project: virtual-produced-service)
 */
package org.virtualrepository.service.rest.resources;

import static org.dynamicvalues.Directives.*;
import static org.dynamicvalues.Dynamic.*;
import static org.virtualrepository.service.Constants.*;

import java.io.StringWriter;
import java.util.Collection;
import java.util.HashSet;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.dynamicvalues.Dynamic;
import org.dynamicvalues.DynamicIO;
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
abstract public class AbstractResource {
	static private VirtualRepository REPO;
	
	static private String[] JSON_DEFAULT_EXCLUSION_PATTERNS = { "class", "*.class" };
	static private XStream XSTREAM;
	static private JAXBContext JAXB_CONTEXT;
	
	static {
		REPO = new Repository();
		REPO.discover(CsvCodelist.type, SdmxCodelist.type);
		
		XSTREAM = new XStream();
		XSTREAM.omitField(RepositoryService.class, "proxy");
		XSTREAM.setMode(XStream.ID_REFERENCES);
		
		JAXB_CONTEXT = DynamicIO.newInstance();
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

	protected String xmlify(Object object) throws Exception {
		StringWriter writer = new StringWriter();
		Marshaller m = JAXB_CONTEXT.createMarshaller();
		m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		
		m.marshal(externalValueOf(object), writer);
		
		writer.flush();
		
		return writer.toString();
	}
	
	protected String vxmlify(Object object) throws Exception {
		return XSTREAM.toXML(object);
	}
	
	protected Response jsonResponse(Object object) throws Exception {
		return this.jsonResponse(object, (String[])null);
	}
	
	protected Response jsonResponse(Object object, String... exclusionPatterns) throws Exception {
		return Response.ok(this.jsonify(object, exclusionPatterns), MediaType.APPLICATION_JSON).build();
	}
	
	protected Response xmlResponse(Object object) throws Exception {
		return Response.ok(this.xmlify(object), MediaType.APPLICATION_XML).build();
	}
	
	protected Response vxmlResponse(Object object) throws Exception {
		return Response.ok(this.vxmlify(object), xobject).build();
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