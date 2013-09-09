package org.acme.utils;

import static org.jboss.shrinkwrap.api.ShrinkWrap.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.net.URL;

import org.acme.utils.TestMocks.AssetBuilder;
import org.acme.utils.TestMocks.ProxyBuilder;
import org.acme.utils.TestMocks.ServiceBuilder;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.mockito.Mockito;
import org.virtualrepository.Asset;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.spi.Publisher;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.LoggingFilter;

@SuppressWarnings("all")
public class TestUtils {

	public static final String warName = "vrs.war";
	public static final String restRoot = "rest";
	
	public static String at(URL base,String resource) {
		return base+restRoot+resource;
	}
	
	public static WebArchive war() {

		WebArchive war = addWebInfResourcesTo(create(WebArchive.class, warName)
				.addAsResource(new File("src/main/resources/configuration.properties"))
				.addAsResource(new File("src/main/resources/logback.xml"))
				.addPackages(true, "org.virtualrepository.service")
				.addAsLibraries(
						Maven.resolver().loadPomFromFile("pom.xml").importRuntimeDependencies().resolve()
								.withTransitivity().asFile())
				);
				
		

		System.out.println(war.toString(true));

		return war;

	}
	
	public static WebArchive warWithTests() {

		WebArchive war = addWebInfResourcesTo(create(WebArchive.class, warName)
				.addAsResource(new File("src/main/resources/configuration.properties"))
				.addAsResource(new File("src/main/resources/logback.xml"))
				.addPackages(true, "org.virtualrepository.service")
				.addPackages(true, "org.acme")
				.addAsLibraries(
						Maven.resolver().loadPomFromFile("pom.xml").importRuntimeAndTestDependencies().resolve()
								.withTransitivity().asFile())
				);
				
		

		System.out.println(war.toString(true));

		return war;

	}
	
	public static Client call() {
		Client client = Client.create();
		client.addFilter(new LoggingFilter(System.err));
		return client;
	}
	
	private static WebArchive addWebInfResourcesTo(WebArchive war) {
		
		//should be replaced with something simpler in later shrinkwrap versions
		return war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
			    .importDirectory("src/main/webapp").as(GenericArchive.class),
			    "/", Filters.includeAll());
	}
	
	public static void main(String[] args) {
		war();
	}
	
	
	/**
	 * Creates a mock proxy
	 * @return a mock builder
	 */
	public static ProxyBuilder aProxy() {
		return new ProxyBuilder();
	}
	
	/**
	 * Creates a service
	 * @return a service builder
	 */
	public static ServiceBuilder aService() {
		return new ServiceBuilder();
	}
	
	/**
	 * Creates a mock asset.
	 * @return the mock builder
	 */
	public static AssetBuilder anAsset() {
		return new AssetBuilder();
	}
	
	/**
	 * Creates a mock type for generic asset type
	 * @return the mock type
	 */
	public static Type<Asset> aType() {
		return aTypeFor(Asset.class);
	}
	
	/**
	 * Creates a mock type for a given asset type
	 * @return the mock type
	 */
	public static <T extends Asset> Type<Asset> aTypeFor(Class<T> assetType) {
		return Mockito.mock(Type.class);
	}

	/**
	 * Creates an importer for a given asset type and the Object API
	 * @param type the type
	 * @return the mock importer
	 */
	public static <T extends Asset> Importer<T,Object> anImporterFor(Type<T> type) {
		return anImporterFor(type,Object.class);
	}

	/**
	 * Creates an importer for a given asset type and API
	 * @param type the type
	 * @param api the API
	 * @return the mock importer
	 */
	public static <T extends Asset, A> Importer<T,A> anImporterFor(Type<T> type, Class<A> api) {
		Importer importer =  Mockito.mock(Importer.class);
		when(importer.type()).thenReturn(type);
		when(importer.api()).thenReturn(api);
		return importer;
	}
}
