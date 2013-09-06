package org.acme;

import static org.junit.Assert.*;

import org.junit.Test;
import org.virtualrepository.service.rest.VrsMediaType;

public class MediaTypeTest {

	@Test
	public void invariants() throws Exception {
		
		for (VrsMediaType type : VrsMediaType.values()) {
			assertNotNull(type.type());
			assertEquals(type.type().toString(),type.toString());
			assertSame(type,VrsMediaType.fromMediaType(type.type()));
		}
	}
		
}
