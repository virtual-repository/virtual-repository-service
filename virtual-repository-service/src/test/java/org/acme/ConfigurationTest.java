package org.acme;

import static org.acme.utils.TestUtils.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.service.configuration.Configuration;

/**
 * Tests the key elements of application infrastructure and testing are in place.
 * 
 * @author Fabio Simeoni
 *
 */
public class ConfigurationTest {

	@Test
	public void isLoaded() throws Exception {
		
		VirtualRepository repo = new Repository();
		
		Configuration c = new Configuration(repo);
		
		assertTrue(c.assetTypes().length==0);
		
		repo.services().add(aService().with(aProxy().get()).get());
		
		assertTrue(c.assetTypes().length==1);
		
		
	}
	
	
}
