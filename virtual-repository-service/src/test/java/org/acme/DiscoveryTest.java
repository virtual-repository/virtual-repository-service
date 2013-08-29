package org.acme;

import static java.util.Arrays.*;
import static javax.ws.rs.core.MediaType.*;
import static org.acme.utils.TestUtils.*;
import static org.dynamicvalues.Dynamic.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.virtualrepository.service.rest.DiscoveryService.*;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;

import org.acme.utils.TestUtils;
import org.dynamicvalues.DynamicIO;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.virtualrepository.Asset;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.service.utils.CdiProducers;
import org.virtualrepository.spi.ServiceProxy;

import flexjson.JSONDeserializer;

/**
 * Tests the key elements of application infrastructure and testing are in place.
 * 
 * @author Fabio Simeoni
 *
 */
@RunWith(Arquillian.class)
public class DiscoveryTest {

	private static List<Asset> csvAssets = new ArrayList<Asset>();
	private static List<Asset> sdmxAssets = new ArrayList<Asset>();
			
	@Deployment(testable = false)
	public static WebArchive deploy() {
		return TestUtils.war();
	}
	
	@BeforeClass
	@SuppressWarnings("all")
	public static void setup() throws Exception {
		
		csvAssets.add(new CsvCodelist("id1","name",0));
		csvAssets.add(new CsvCodelist("id2","name",0));
		sdmxAssets.add(new SdmxCodelist("urn://acme.org","id2","1.0","name"));
		
		ServiceProxy proxy1 = aProxy().with(anImporterFor(CsvCodelist.type)).get();
		ServiceProxy proxy2 = aProxy().with(anImporterFor(SdmxCodelist.type)).get();
		
		RepositoryService service1 = aService().with(proxy1).get();
		
		when(proxy1.browser().discover(asList(CsvCodelist.type))).thenReturn((Iterable)csvAssets);
		
		RepositoryService service2 = aService().with(proxy2).get();
		when(proxy2.browser().discover(asList(SdmxCodelist.type))).thenReturn((Iterable)sdmxAssets);
		
		CdiProducers.repository().services().add(service1,service2);
	}
	
	
	@Test
	public void inJson(@ArquillianResource URL context) throws Exception {
		
		String outcome = call().resource(at(context,path)).accept(APPLICATION_JSON).get(String.class);
		
		JSONDeserializer<List<?>> deserializer = new JSONDeserializer<List<?>>();
		
		List<?> list = deserializer.deserialize(outcome);
		
		assertEquals(csvAssets.size()+sdmxAssets.size(),list.size());
		
	}
	
	@Test
	public void selectiveInJson(@ArquillianResource URL context) throws Exception {
		
		System.err.println(at(context,path));
		String outcome = call().resource(at(context,path)).queryParam(typeParam,CsvCodelist.type.name()).accept(APPLICATION_JSON).get(String.class);
		
		JSONDeserializer<List<?>> deserializer = new JSONDeserializer<List<?>>();
		
		List<?> list = deserializer.deserialize(outcome);
		
		assertEquals(csvAssets.size(),list.size());
		
	}
	
	@Test
	public void inXml(@ArquillianResource URL context) throws Exception {
		
		String outcome = call().resource(at(context,path)).accept(APPLICATION_XML).get(String.class);
		
		Unmarshaller um = DynamicIO.newInstance().createUnmarshaller();
		
		//parse it as clean map
		List<?>  list = valueOf(um.unmarshal(new StringReader(outcome)));
		
		assertEquals(csvAssets.size()+sdmxAssets.size(),list.size());
	}
	
	
}