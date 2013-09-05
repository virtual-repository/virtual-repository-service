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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;

import org.acme.InfrastructureTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.VirtualRepository;


@Path("/test")
@Singleton
public class TestService  {

	private static final Logger log = LoggerFactory.getLogger(TestService.class);
	
	public static final String inject_path = "/inject";
	public static final String negotiated_path = "/negotiated";
	
	@Inject
	VirtualRepository bean;
	
	
	@GET
	@Path(inject_path)
	public Response injected() {
		
		//logging works
		log.info("logging setup");
		
		//injection works
		assertNotNull(bean);
		
		//repository is staged for test
		bean.services().contains(new QName(InfrastructureTest.test_service));
		
		return ok().build();
	}
	
	@GET
	@Path(negotiated_path)
	@Produces(MediaType.APPLICATION_JSON)
	public Object negotiated() {
		
		@SuppressWarnings("unused")
		Object outcome = new Object(){
			String foo="bar";
		};
		return ok(outcome).build();
	}
}