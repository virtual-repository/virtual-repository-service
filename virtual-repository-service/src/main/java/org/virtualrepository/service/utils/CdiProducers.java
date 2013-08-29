package org.virtualrepository.service.utils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.virtualrepository.VirtualRepository;
import org.virtualrepository.impl.Repository;

/**
 * Makes available 3rd party beans to CDI for injection
 * 
 * @author Fabio Simeoni
 *
 */
public class CdiProducers {

	//use a real singletong can get hold of this from tests
	private static VirtualRepository repository = new Repository();
	
	/**
	 * Shared {@link VirtualRepository} instance.
	 * @return the instance
	 */
	@Produces @ApplicationScoped  
	public static VirtualRepository repository() {
		return repository;
	}
}
