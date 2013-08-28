package org.acme;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfrastructureTest {

	
	@Test
	public void loggingIsConfigured() {

		Logger log = LoggerFactory.getLogger("org.virtualrepository");
		log.info("test");
	}
}
