package org.acme;

import static javax.ws.rs.core.MediaType.*;
import static org.acme.utils.TestUtils.*;
import static org.dynamicvalues.Dynamic.*;
import static org.virtualrepository.service.rest.ServiceDescriptionResource.*;

import java.io.StringReader;
import java.net.URL;
import java.util.Map;

import javax.xml.bind.Unmarshaller;

import org.acme.utils.TestUtils;
import org.dynamicvalues.DynamicIO;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import flexjson.JSONDeserializer;

/**
 * Tests the key elements of application infrastructure and testing are in place.
 * 
 * @author Fabio Simeoni
 *
 */
@RunWith(Arquillian.class)
public class ServiceDescriptionTest {

	@Deployment(testable = false)
	public static WebArchive deploy() {
		return TestUtils.war();
	}
	
	@Test
	public void inJson(@ArquillianResource URL context) throws Exception {
		
		//note: tests are independent of service URIs
		
		String outcome = call().resource(at(context,path)).accept(APPLICATION_JSON).get(String.class);
		
		JSONDeserializer<Map<?,?>> deserializer = new JSONDeserializer<Map<?,?>>();
		
		Map<?,?> map = deserializer.deserialize(outcome);
		
		System.out.println(map);
		
	}
	
	@Test
	public void inXml(@ArquillianResource URL context) throws Exception {
		
		String outcome = call().resource(at(context,path)).accept(APPLICATION_XML).get(String.class);
		
		Unmarshaller um = DynamicIO.newInstance().createUnmarshaller();
		
		//parse it as clean map
		Map<?,?> map = valueOf(um.unmarshal(new StringReader(outcome)));
		
		System.out.println(map);
	}
	
	
}
