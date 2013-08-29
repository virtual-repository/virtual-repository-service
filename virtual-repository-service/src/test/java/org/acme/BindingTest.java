package org.acme;

import static org.dynamicvalues.Dynamic.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.virtualrepository.Asset;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.service.io.Binder;

/**
 * Tests the key elements of application infrastructure and testing are in place.
 * 
 * @author Fabio Simeoni
 *
 */
public class BindingTest {

	@Test
	public void assetsCanBeBoundToJson() throws Exception {
		
		Asset a = 	new CsvCodelist("id1","name",0);
		
		Binder binder = new Binder();
		
		binder.jsonMoM(valueOf(a));
		
	}
	
	@Test
	public void assetsCanBeBoundToXml() throws Exception {
		
		Asset a1 = 	new CsvCodelist("id1","name1",0);
		Asset a2 = 	new CsvCodelist("id2","name2",0);
		
		List<Asset> assets = new ArrayList<Asset>();
		
		assets.add(a1);
		assets.add(a2);
		
		Binder binder = new Binder();
		
		binder.xmlMoM(valueOf(assets));
		
	}
	
	
}
