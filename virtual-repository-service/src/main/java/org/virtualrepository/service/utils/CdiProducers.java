package org.virtualrepository.service.utils;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.sdmx.SdmxServiceFactory;
import org.sdmxsource.sdmx.api.manager.output.StructureWriterManager;
import org.sdmxsource.sdmx.api.manager.parse.StructureParsingManager;
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
	
	/**
	 * Returns a {@link StructureParsingManager} service.
	 * 
	 * @return the service
	 */
	@Produces @Singleton
	public static StructureParsingManager parser() {
		return SdmxServiceFactory.parser();
	}

	/**
	 * Returns a {@link StructureWritingManager} service.
	 * 
	 * @return the service
	 */
	@Produces @Singleton
	public static StructureWriterManager writer() {
		return SdmxServiceFactory.writer();
	}
}
