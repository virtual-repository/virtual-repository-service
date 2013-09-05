package org.virtualrepository.service.utils;

import static org.virtualrepository.service.Constants.*;

import java.util.HashSet;
import java.util.Set;

import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.AssetType;
import org.virtualrepository.tabular.Table;

/**
 * App-wide utilities.
 * 
 * @author Fabio Simeoni
 *
 */
public class Utils {


	public static RuntimeException unchecked(Throwable t) {

		return unchecked(t.getMessage()+"( unchecked wrapper )",t);

	}
	
	public static RuntimeException unchecked(String msg, Throwable t) {

		return (t instanceof RuntimeException) ? RuntimeException.class.cast(t) : new RuntimeException(msg,
				t);

	}
	
	public static RuntimeException wrapped(String msg, Throwable t) {

		return new RuntimeException(msg,t);

	}

	public static void rethrow(String msg,Throwable t) throws RuntimeException {
		
		throw wrapped(msg,t);

	}
	
	public static void rethrowUnchecked(String msg,Throwable t) throws RuntimeException {

		throw unchecked(msg,t);

	}
	
	public static void rethrowUnchecked(Throwable t) throws RuntimeException {

		throw unchecked(t);

	}
	
	
	/**
	 * Returns the asset type among many which has a given standardName.
	 * @param types the types
	 * @param standardName the standardName
	 * @return the asset type with the given standardName
	 */
	static public AssetType typeWith(AssetType[] types, String name) {
		
		for(AssetType type : types)
			if(type.name().equals(name))
				return type;
		
		throw new IllegalArgumentException("unknown asset type '" + name + "'");
	}
	
	static public Class<?> apiForName(String name) throws RuntimeException {
		if(TABLE.equals(name))
			return Table.class;
		else if(SDMX_SS.equals(name))
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
		
		if(SDMX_SS.equals(model) && acceptedMediaTypes.contains(APPLICATION_SDMX_GENERIC_DATA)) {
			return APPLICATION_SDMX_GENERIC_DATA;
		}
		
		for(String preferredMediaType : ALL_MEDIA_TYPES)
			if(acceptedMediaTypes.contains(preferredMediaType))
				return preferredMediaType;
		
		throw new RuntimeException("Accept header lists no valid media type for model '" + model + "'");
	}
}
