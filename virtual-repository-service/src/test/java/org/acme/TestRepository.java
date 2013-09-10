package org.acme;

import static java.util.Arrays.*;
import static org.acme.utils.TestUtils.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.sdmx.CodelistBuilder.*;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.acme.utils.TestUtils;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.Asset;
import org.virtualrepository.Property;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.service.utils.Fake;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.spi.ServiceProxy;
import org.virtualrepository.tabular.Table;

public class TestRepository {
	
	public static List<Asset> csvAssets = new ArrayList<Asset>();
	public static List<Asset> sdmxAssets = new ArrayList<Asset>();
	
	public static String csv_id1 = "id1";
	public static CsvCodelist csv_1;
	public static CsvCodelist csv_2;
	public static String csv_id2 = "id2";
	public static String sdmx_id1 = "urn1";
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

		Table table = TestUtils.twoBytwoTable();
		Importer<Asset,Object> importer = (Importer) anImporterFor(CsvCodelist.type,Table.class);
		when(importer.retrieve(any(Asset.class))).thenReturn(table);
		ServiceProxy proxy1 = aProxy().with(importer).get();
		RepositoryService service1 = aService().with(proxy1).get();
		when(proxy1.browser().discover(asList(CsvCodelist.type))).thenReturn((Iterable) csvAssets);

		CodelistBean list = list().add(code("id").name("name","en")).end();
		importer = (Importer) anImporterFor(SdmxCodelist.type,CodelistBean.class);
		when(importer.retrieve(any(Asset.class))).thenReturn(list);
		ServiceProxy proxy2 = aProxy().with(importer).get();
		RepositoryService service2 = aService().with(proxy2).get();

		when(proxy2.browser().discover(asList(SdmxCodelist.type))).thenReturn((Iterable) sdmxAssets);
		
		return new Repository(service1,service2);
		
	}
	

}
