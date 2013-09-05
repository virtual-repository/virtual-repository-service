package org.acme;

import static com.sun.jersey.api.client.ClientResponse.Status.*;
import static javax.ws.rs.core.MediaType.*;
import static org.acme.utils.TestUtils.*;
import static org.junit.Assert.*;

import java.net.URL;

import org.acme.utils.TestUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.service.rest.TestService;
import org.virtualrepository.service.utils.CdiProducers;
import org.virtualrepository.spi.ServiceProxy;

import com.sun.jersey.api.client.ClientResponse;

/**
 * Tests the key elements of application infrastructure and testing are in place.
 * 
 * @author Fabio Simeoni
 * 
 */
@RunWith(Arquillian.class)
public class InfrastructureTest {

	public static final String test_service = "test-service";
	
	static final String testPath = "/test";
	static final String negotiated = testPath + TestService.negotiated_path;
	static final String injected = testPath + TestService.inject_path;




	@Deployment(testable = false)
	public static WebArchive deploy() {
		return TestUtils.war();
	}

	@Test
	public void injected(@ArquillianResource URL root) throws Exception {

		CdiProducers.repository().services().add(mockRepository());

		// assertion of injection is in test servlet
		call().resource(at(root,injected)).get(String.class);

	}

	@Test
	public void negotiatedBasedOnPreference(@ArquillianResource URL root) throws Exception {

		ClientResponse response = call().resource(at(root, negotiated)).get(ClientResponse.class);

		assertEquals(OK, response.getClientResponseStatus());
		assertEquals(APPLICATION_JSON_TYPE, response.getType());

	}

	@Test
	public void negotiatedBasedOnDefault(@ArquillianResource URL root) throws Exception {

		ClientResponse response = call().resource(at(root, negotiated)).get(ClientResponse.class);

		assertEquals(OK, response.getClientResponseStatus());
		assertEquals(APPLICATION_JSON_TYPE, response.getType());

	}

	@Test
	public void unnegotiated(@ArquillianResource URL root) throws Exception {

		ClientResponse response  = call().resource(at(root,negotiated)).accept("text/unsupported").get(ClientResponse.class);
		
		assertEquals(NOT_ACCEPTABLE, response.getClientResponseStatus());

	}

	// helper
	private RepositoryService mockRepository() {

		ServiceProxy proxy = aProxy().get();
		return aService().name(test_service).with(proxy).get();

	}
}
