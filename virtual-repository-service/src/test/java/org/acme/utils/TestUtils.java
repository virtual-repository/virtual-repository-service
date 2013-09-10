package org.acme.utils;

import static java.util.Arrays.*;
import static org.jboss.shrinkwrap.api.ShrinkWrap.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

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
import org.virtualrepository.Property;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.spi.Publisher;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.DefaultTable;
import org.virtualrepository.tabular.Row;
import org.virtualrepository.tabular.Table;

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
				.addAsResource(new File("src/test/resources/vrs.properties"))
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
	
	
	public static Table twoBytwoTable() {
		
		List<String[]> csv = new ArrayList<String[]>();
		for (int c =0; c<2;c++) {
			List<String> row = new ArrayList<String>();
			for (int r=0;r<2;r++)
				row.add(""+r+c);
			csv.add(row.toArray(new String[0]));
		}
		
		String[][] data = csv.toArray(new String[0][]);
		
		
		Column col1 = new Column(new QName("http://acme.org","col1"));
		col1.setKind(new QName("http://acme.org","name"));
		col1.properties().add(new Property("prop1","val1"));
		Column col2 = new Column(new QName("col2"));
		
		Column[] columns = new Column[]{col1,col2};
		List<Row> rows = new ArrayList<Row>();
		for (String[] row : data) {
			Map<QName,String> map = new HashMap<QName, String>(); 
			for (int i=0;i<row.length;i++)
					map.put(columns[i].name(),row[i]);
			
			rows.add(new Row(map));	
		}
		
		return new DefaultTable(asList(columns),rows);
	}
}
