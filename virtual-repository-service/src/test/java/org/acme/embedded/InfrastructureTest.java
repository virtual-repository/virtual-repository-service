package org.acme.embedded;

import static com.sun.jersey.api.client.ClientResponse.Status.*;
import static javax.ws.rs.core.HttpHeaders.*;
import static javax.ws.rs.core.MediaType.*;
import static org.acme.utils.TestUtils.*;
import static org.junit.Assert.*;
import static org.virtualrepository.service.rest.TestService.*;
import static org.virtualrepository.service.rest.VrsMediaType.*;

import java.net.URL;

import org.acme.utils.TestUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

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
	
	@Deployment(testable = false)
	public static WebArchive deploy() {
		return TestUtils.war();
	}

	@Test
	public void injected(@ArquillianResource URL root) throws Exception {

		// assertion of injection is in test servlet
		call().resource(at(root,path+injected_path)).get(String.class);

	}

	@Test
	public void negotiatedBasedOnPreference(@ArquillianResource URL root) throws Exception {

		ClientResponse response = call().resource(at(root,path+negotiated_path)).accept(JMOM.toString()).get(ClientResponse.class);

		assertEquals(OK, response.getClientResponseStatus());
		assertEquals(APPLICATION_JSON_TYPE, response.getType());

	}

	@Test
	public void negotiatedBasedOnDefault(@ArquillianResource URL root) throws Exception {

		ClientResponse response = call().resource(at(root,path+negotiated_path)).get(ClientResponse.class);

		assertEquals(OK, response.getClientResponseStatus());
		assertEquals(APPLICATION_JSON_TYPE, response.getType());

	}

	@Test
	public void unnegotiated(@ArquillianResource URL root) throws Exception {

		ClientResponse response  = call().resource(at(root,path+negotiated_path)).accept("text/unsupported").get(ClientResponse.class);
		
		assertEquals(NOT_ACCEPTABLE, response.getClientResponseStatus());

	}
	
	@Test
	public void adapted(@ArquillianResource URL root) throws Exception {

		ClientResponse response  = call().resource(at(root,path+adapted_path))
				.queryParam("foo","bar") //'native' params are retained
				.queryParam(ACCEPT,JMOM.toString()) //'header' params are added to headers
				.accept(XMOM.type()) //existing headers are retained
				.get(ClientResponse.class);
		
		assertEquals(OK.getStatusCode(), response.getStatus());
		
		assertEquals(JMOM.toString(), response.getHeaders().getFirst(CONTENT_TYPE));

	}
}
