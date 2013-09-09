package org.acme;

import static java.util.Arrays.*;
import static org.acme.utils.TestUtils.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.virtualrepository.Asset;
import org.virtualrepository.Property;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.service.utils.Fake;
import org.virtualrepository.spi.ServiceProxy;

public class TestRepository {
	
	public static List<Asset> csvAssets = new ArrayList<Asset>();
	public static List<Asset> sdmxAssets = new ArrayList<Asset>();
	
	public static String csv_id1 = "id1";
	public static CsvCodelist csv_1;
	public static CsvCodelist csv_2;
	public static String csv_id2 = "id2";
	public static String sdmx_id1 = "urn://acme.org";
	public static SdmxCodelist sdmx_1;
	
	public static final String test_service = "test-service";
	
	@Produces @Fake @Singleton  
	@SuppressWarnings("all")
	public static VirtualRepository repository() throws Exception {
		
		
		csv_1 = new CsvCodelist(csv_id1, "standardName", 0);
		Property privateProperty = new Property("private","hello");
		privateProperty.display(false);
		csv_1.properties().add(new Property("public","hello"), privateProperty);
		csvAssets.add(csv_1);
		
		csv_2 = new CsvCodelist(csv_id2, "standardName", 0);
		csvAssets.add(csv_2);
		
		sdmx_1= new SdmxCodelist(sdmx_id1, "id", "1.0", "standardName");
		sdmxAssets.add(sdmx_1);

		ServiceProxy proxy1 = aProxy().with(anImporterFor(CsvCodelist.type)).get();
		ServiceProxy proxy2 = aProxy().with(anImporterFor(SdmxCodelist.type)).get();

		RepositoryService service1 = aService().with(proxy1).get();

		when(proxy1.browser().discover(asList(CsvCodelist.type))).thenReturn((Iterable) csvAssets);

		RepositoryService service2 = aService().with(proxy2).get();
		when(proxy2.browser().discover(asList(SdmxCodelist.type))).thenReturn((Iterable) sdmxAssets);
		
		return new Repository(service1,service2);
		
	}
	
	
}
