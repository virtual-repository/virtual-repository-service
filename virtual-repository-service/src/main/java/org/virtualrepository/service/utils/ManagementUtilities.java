/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.service.utils;

import static org.virtualrepository.service.Constants.*;

import java.util.HashSet;
import java.util.Set;

import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.AssetType;
import org.virtualrepository.service.rest.ModelConstants;
import org.virtualrepository.tabular.Table;

/**
 * Place your class / interface description here.
 *
 * History:
 *
 * ------------- --------------- -----------------------
 * Date			 Author			 Comment
 * ------------- --------------- -----------------------
 * 27 Aug 2013   Fiorellato     Creation.
 *
 * @version 1.0
 * @since 27 Aug 2013
 */
final public class ManagementUtilities {
	private ManagementUtilities() { }
	
	static public AssetType typeWith(AssetType[] types, String name) {
	
		if(types == null || types.length == 0)
			throw new RuntimeException("Please provide a non-NULL and non-empty list of available asset types");
		
		if(name == null)
			throw new RuntimeException("Please provide a non-NULL asset type name");
		
		for(AssetType type : types)
			if(type.name().equals(name))
				return type;
		
		throw new IllegalArgumentException("Unknown asset type '" + name + "'");
	}
	
	static public Class<?> apiForName(String name) throws RuntimeException {
		if(ModelConstants.TABLE.equals(name))
			return Table.class;
		else if(ModelConstants.SDMX_SS.equals(name))
			return CodelistBean.class;
		
		throw new IllegalArgumentException("Unknown / unavailable api model '" + name + "'");
	}
	
	static public String getProperMediaTypeFor(String model, String acceptHeader) {
		if(acceptHeader == null || model == null)
			return null;
		
		acceptHeader = acceptHeader.replaceAll(";.+$", "");
				
		Set<String> acceptedMediaTypes = new HashSet<String>(); 
		for(String type : acceptHeader.split("\\,")) {
			type = type.trim();
			
			if(type.length() > 0)
				acceptedMediaTypes.add(type);
		}
		
		if(ModelConstants.SDMX_SS.equals(model) && acceptedMediaTypes.contains(APPLICATION_SDMX_GENERIC_DATA)) {
			return APPLICATION_SDMX_GENERIC_DATA;
		}
		
		for(String preferredMediaType : PREFERRED_MEDIA_TYPES)
			if(acceptedMediaTypes.contains(preferredMediaType))
				return preferredMediaType;
		
		throw new RuntimeException("Accept header lists no valid media type for model '" + model + "'");
	}
}