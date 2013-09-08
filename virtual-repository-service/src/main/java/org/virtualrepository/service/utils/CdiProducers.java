package org.virtualrepository.service.utils;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.virtualrepository.VirtualRepository;
import org.virtualrepository.impl.Repository;

/**
 * Makes available 3rd party beans to CDI for injection, optionally {@link Fake} ones during testing.
 * 
 * @author Fabio Simeoni
 *
 */
//we want it to use @Fake deps when testing, but real deps in production
//so we inject optional @Fake deps in producer methods, and if these do not exit we produce real deps.
public class CdiProducers {

	public static final String test="test";
	
	//make it accessible to unmanaged beans (e.g. servlet listeners)
	public static VirtualRepository produced;

	@Produces @Singleton 
	public VirtualRepository repository(@Fake Instance<VirtualRepository> testRepo) {
		
		produced =  testRepo.isUnsatisfied()?new Repository():testRepo.get();
				
		return produced;
	}
}
