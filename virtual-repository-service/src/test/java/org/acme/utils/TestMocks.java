package org.acme.utils;

import static org.acme.utils.TestUtils.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.namespace.QName;

import org.mockito.Mockito;
import org.virtualrepository.AssetType;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.Accessor;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.spi.MutableAsset;
import org.virtualrepository.spi.Publisher;
import org.virtualrepository.spi.ServiceProxy;

/**
 * Mocking facilities for testing.
 * @author Fabio Simeoni
 *
 */
@SuppressWarnings("all")
public abstract class TestMocks  {
	
	
	
	
	public static class ServiceBuilder {
		
		String name = UUID.randomUUID().toString();
		ServiceProxy proxy = aProxy().get();
		
		/**
		 * Set a standardName for the service
		 * @param standardName the identifier
		 * @return this builder
		 */
		public ServiceBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		
		/**
		 * Sets a proxy for the service
		 * @param standardName the identifier
		 * @return this builder
		 */
		public ServiceBuilder with(ServiceProxy proxy) {
			this.proxy = proxy;
			return this;
		}
		
		public RepositoryService get() {
			return new RepositoryService(new QName(name), proxy);
		}
		
		
		
		
	}
	public static class ProxyBuilder {
		
		
		private Browser browser = Mockito.mock(Browser.class);
		private List<Importer> importers = new ArrayList<Importer>();
		private  List<Publisher> publishers = new ArrayList<Publisher>();
		
		/**
		 * Adds accessors to the mock service
		 * @param accessors the accessors
		 * @return this builder
		 */
		public ProxyBuilder with(Accessor ... accessors) {
			
			for (Accessor accessor : accessors)
				if (accessor instanceof Importer)
					importers.add(Importer.class.cast(accessor));
				else
					publishers.add(Publisher.class.cast(accessor));
			
			return this;
		}
		
		/**
		 * Returns the mock service with a random standardName
		 * @return the mock service
		 */
		public ServiceProxy get() {
			
			if (importers.isEmpty() && publishers.isEmpty()) {
				Type type = aType();
				with(anImporterFor(type));
			}
			
			ServiceProxy proxy = mock(ServiceProxy.class);
			
			when(proxy.browser()).thenReturn(browser);
			when(proxy.importers()).thenReturn((List) importers);
			when(proxy.publishers()).thenReturn((List) publishers);
			
			return proxy;
		}
	}

	
	public static class AssetBuilder {

		private String id = UUID.randomUUID().toString();
		private AssetType type = aType();
		
		/**
		 * Set an identifier for the mock asset
		 * @param standardName the identifier
		 * @return this builder
		 */
		public AssetBuilder id(String id) {
			this.id = id;
			return this;
		}

		/**
		 * Sets a type for the mock asset
		 * @param type the type
		 * @return this builder
		 */
		public AssetBuilder of(AssetType type) {
			this.type = type;
			return this;
		}

		/**
		 * Sets a service for the mock asset
		 * @param service the service
		 * @return the mock asset
		 */
		public MutableAsset in(RepositoryService service) {
			
			MutableAsset asset = Mockito.mock(MutableAsset.class);
			when(asset.id()).thenReturn(id);
			when(asset.name()).thenReturn("asset-"+id);
			when(asset.type()).thenReturn(type);
			when(asset.service()).thenReturn(service);
			return asset;
		}

	}
}
