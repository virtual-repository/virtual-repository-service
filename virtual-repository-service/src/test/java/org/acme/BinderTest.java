package org.acme;

import static org.dynamicvalues.Dynamic.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.virtualrepository.Asset;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.service.io.Binder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class BinderTest {

	@Test
	public void assetsCanBeBoundToJson() throws Exception {
		
		Asset a = 	new CsvCodelist("id1","standardName",0);
		
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
	
	
	@Test
	public void assetsCanBeBoundToVXml() throws Exception {
		
		Asset a1 = 	new CsvCodelist("id1","name1",0);
		Asset a2 = 	new CsvCodelist("id2","name2",0);
		
		List<Asset> assets = new ArrayList<Asset>();
		
		assets.add(a1);
		assets.add(a2);
		
		Binder binder = new Binder();
		
		String bound = binder.vxml(assets);
		
		List<?> read = (List<?>) new XStream(new StaxDriver()).fromXML(bound);
		
		//System.out.println(read);
		
		Assert.assertEquals(assets.get(0),read.get(0));
	}
	
	
}
