package org.acme;

import static com.sun.jersey.api.client.ClientResponse.Status.*;
import static java.util.Arrays.*;
import static javax.ws.rs.core.HttpHeaders.*;
import static org.acme.utils.TestUtils.*;
import static org.dynamicvalues.Dynamic.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.virtualrepository.service.Constants.*;
import static org.virtualrepository.service.rest.AssetsResource.*;
import static org.virtualrepository.service.rest.VrsMediaType.*;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.EntityTag;
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

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import flexjson.JSONDeserializer;


@RunWith(Arquillian.class)
public class AssetsTest {

	private static List<Asset> csvAssets = new ArrayList<Asset>();
	private static List<Asset> sdmxAssets = new ArrayList<Asset>();

	@Deployment(testable = false)
	public static WebArchive deploy() {
		return TestUtils.war();
	}

	@BeforeClass
	@SuppressWarnings("all")
	public static void setup() throws Exception {

		csvAssets.add(new CsvCodelist("id1", "standardName", 0));
		csvAssets.add(new CsvCodelist("id2", "standardName", 0));
		sdmxAssets.add(new SdmxCodelist("urn://acme.org", "id2", "1.0", "standardName"));

		ServiceProxy proxy1 = aProxy().with(anImporterFor(CsvCodelist.type)).get();
		ServiceProxy proxy2 = aProxy().with(anImporterFor(SdmxCodelist.type)).get();

		RepositoryService service1 = aService().with(proxy1).get();

		when(proxy1.browser().discover(asList(CsvCodelist.type))).thenReturn((Iterable) csvAssets);

		RepositoryService service2 = aService().with(proxy2).get();
		when(proxy2.browser().discover(asList(SdmxCodelist.type))).thenReturn((Iterable) sdmxAssets);

		CdiProducers.repository().services().add(service1, service2);
	}

	@Test
	public void inJson(@ArquillianResource URL context) throws Exception {

		String outcome = call().resource(at(context, path)).accept(JMOM.type()).get(String.class);

		JSONDeserializer<List<?>> deserializer = new JSONDeserializer<List<?>>();

		List<?> list = deserializer.deserialize(outcome);

		assertEquals(csvAssets.size() + sdmxAssets.size(), list.size());

	}
	
	
	@Test
	public void usingTheDefaultType(@ArquillianResource URL context) throws Exception {

		String outcome = call().resource(at(context, path)).get(String.class);

		JSONDeserializer<List<?>> deserializer = new JSONDeserializer<List<?>>();

		List<?> list = deserializer.deserialize(outcome);

		assertEquals(csvAssets.size() + sdmxAssets.size(), list.size());

	}
	
	@Test
	public void usingUnsupportedType(@ArquillianResource URL context) throws Exception {

		try {
			call().resource(at(context, path)).accept("unsupported").get(String.class);
			fail();
		}
		catch(UniformInterfaceException e) {
			assertEquals(NOT_ACCEPTABLE,e.getResponse().getClientResponseStatus());
		}


	}


	@Test
	public void withSelectedTypesInJson(@ArquillianResource URL context) throws Exception {

		String outcome = call().resource(at(context, path)).queryParam(typeParam, CsvCodelist.type.name())
				.accept(JMOM.type()).get(String.class);

		JSONDeserializer<List<?>> deserializer = new JSONDeserializer<List<?>>();

		List<?> list = deserializer.deserialize(outcome);

		assertEquals(csvAssets.size(), list.size());

	}

	@Test
	public void selectingAllTypeIsSameAsNotSelectingThemAtAll_InJson(@ArquillianResource URL context) throws Exception {

		String outcome = call().resource(at(context, path)).queryParam(typeParam, CsvCodelist.type.name())
				.queryParam(typeParam, SdmxCodelist.type.name()).accept(JMOM.type()).get(String.class);

		JSONDeserializer<List<?>> deserializer = new JSONDeserializer<List<?>>();

		List<?> list = deserializer.deserialize(outcome);

		assertEquals(csvAssets.size() + sdmxAssets.size(), list.size());

	}

	@Test
	public void selectingUnknownTypesIsa400(@ArquillianResource URL context) throws Exception {

		ClientResponse response = call().resource(at(context, path)).queryParam(typeParam, "bad")
				.accept(JMOM.type()).get(ClientResponse.class);
		
		assertEquals(BAD_REQUEST, response.getClientResponseStatus());
		

	}

	@Test
	public void withRefreshInJson(@ArquillianResource URL context) throws Exception {

		String outcome = call().resource(at(context, path)).accept(JMOM.type()).get(String.class);

		JSONDeserializer<List<?>> deserializer = new JSONDeserializer<List<?>>();

		List<?> list = deserializer.deserialize(outcome);

		int current = list.size();

		csvAssets.add(new CsvCodelist("idnew", "standardName", 0));

		outcome = call().resource(at(context, path)).accept(JMOM.type()).method("POST", String.class);

		list = deserializer.deserialize(outcome);

		assertEquals(current + 1, list.size());

	}
	
	
	@Test
	public void conditionallyWithLastModified(@ArquillianResource URL context) throws Exception {

		WebResource resource = call().resource(at(context, path));

		ClientResponse response = resource.accept(xobject).get(ClientResponse.class);

		Date lm = response.getLastModified();
		
		response = resource.header(IF_MODIFIED_SINCE,lm).get(ClientResponse.class);

		assertEquals(NOT_MODIFIED,response.getClientResponseStatus());
		
	}
	
	@Test
	public void conditionallyWithEtag(@ArquillianResource URL context) throws Exception {

		WebResource resource = call().resource(at(context, path));

		ClientResponse response = resource.accept(JMOM.type()).get(ClientResponse.class);

		EntityTag tag = response.getEntityTag();
		
		response = resource.header(IF_NONE_MATCH,tag).get(ClientResponse.class);

		assertEquals(NOT_MODIFIED,response.getClientResponseStatus());
		
	}
	
	

	@Test
	public void inXml(@ArquillianResource URL context) throws Exception {

		String outcome = call().resource(at(context, path)).accept(XMOM.type()).get(String.class);

		Unmarshaller um = DynamicIO.newInstance().createUnmarshaller();

		// parse it as clean map
		List<?> list = valueOf(um.unmarshal(new StringReader(outcome)));

		assertEquals(csvAssets.size() + sdmxAssets.size(), list.size());
	}

	@Test
	public void inVXml(@ArquillianResource URL context) throws Exception {

		String outcome = call().resource(at(context, path)).accept(XOBJECT.type()).get(String.class);

		List<?> list = (List<?>) new XStream(new StaxDriver()).fromXML(outcome);

		assertEquals(csvAssets.size() + sdmxAssets.size(), list.size());

	}

}
