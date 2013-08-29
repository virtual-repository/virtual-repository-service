package org.virtualrepository.service.io;

import static org.dynamicvalues.Directives.*;
import static org.virtualrepository.service.utils.Utils.*;

import java.io.StringWriter;

import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.dynamicvalues.Directives;
import org.dynamicvalues.Dynamic;
import org.dynamicvalues.DynamicIO;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.spi.ServiceProxy;

import com.thoughtworks.xstream.XStream;

import flexjson.JSONSerializer;

@Singleton
public class Binder {

	static private String[] JSON_DEFAULT_EXCLUSION_PATTERNS = {"class", "*.class" };
	
	static private Directives defaultDynamicDirectives = by().excluding(type(ServiceProxy.class));
	
	
	private final JSONSerializer jsonOut;
	private final JAXBContext xmlOut;
	private final XStream vxmlOut;
	
	
	public Binder() {
		jsonOut = new JSONSerializer().exclude(JSON_DEFAULT_EXCLUSION_PATTERNS);
		xmlOut = DynamicIO.newInstance();
		
		vxmlOut = new XStream();
		vxmlOut.omitField(RepositoryService.class, "proxy");
		vxmlOut.setMode(XStream.ID_REFERENCES);
		
	}
	
	public String jsonMoM(Object object) {
		
		try {
			return jsonOut.deepSerialize(valueOf(object));
		}
		catch(Exception e) {
			throw wrapped("cannot serialise "+object+" to Json (see cause)",e);
		}
		
	}
	
	public String xmlMoM(Object object) {
		
		try {
			StringWriter writer = new StringWriter();
			Marshaller marshaller = xmlOut.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			marshaller.marshal(externalValueOf(object),writer);
			return writer.toString();
		}
		catch(Exception e) {
			throw wrapped("cannot serialise "+object+" to XML (see cause)",e);
		}
		
	}
	
	public String vxml(Object object) {
		return vxmlOut.toXML(object);
	}
	
	private Object valueOf(Object object) throws Exception {
		return Dynamic.valueOf(object,defaultDynamicDirectives);
	}
	
	private Object externalValueOf(Object object) throws Exception {
		return Dynamic.externalValueOf(object,defaultDynamicDirectives);
	}
}
