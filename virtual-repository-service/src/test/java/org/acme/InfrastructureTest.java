package org.acme;

import static org.acme.utils.TestUtils.*;

import java.net.URL;

import org.acme.utils.TestUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.service.rest.CdiService;
import org.virtualrepository.service.utils.CdiProducers;
import org.virtualrepository.spi.ServiceProxy;

import com.sun.jersey.api.client.Client;

/**
 * Tests the key elements of application infrastructure and testing are in place.
 * 
 * @author Fabio Simeoni
 *
 */
@RunWith(Arquillian.class)
public class InfrastructureTest {

	public static final String test_service="test-service";
	
	@Deployment(testable = false)
	public static WebArchive deploy() {
		return TestUtils.war();
	}
	
	@Test
	public void isReady(@ArquillianResource URL root) throws Exception {
		
		
		CdiProducers.repository().services().add(mockRepository());

		//assertion of injection is in test servlet
		Client.create().resource(at(root,CdiService.path)).get(String.class);
		
	}
	
	private RepositoryService mockRepository() {
		
		ServiceProxy proxy = aProxy().get();
		return aService().name(test_service).with(proxy).get();
		
	}
}
