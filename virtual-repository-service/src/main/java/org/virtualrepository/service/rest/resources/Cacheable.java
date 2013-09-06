package org.virtualrepository.service.rest.resources;

import java.util.Calendar;

public interface Cacheable {

	String etag();
	
	Calendar lastModified();
	
}
