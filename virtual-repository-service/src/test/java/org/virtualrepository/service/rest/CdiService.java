/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.service.rest;

import static javax.ws.rs.core.Response.*;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;

import org.acme.InfrastructureTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.VirtualRepository;


@Path("/test")
@Singleton
public class CdiService  {

	private static final Logger log = LoggerFactory.getLogger(CdiService.class);
	
	public static final String path = "/cdi";
	
	@Inject
	VirtualRepository bean;
	
	
	@GET
	@Path(path)
	public Response test() {
		
		//logging works
		log.info("logging setup");
		
		//injection works
		assertNotNull(bean);
		
		//repository is staged for test
		bean.services().contains(new QName(InfrastructureTest.test_service));
		
		return ok().build();
	}
}
