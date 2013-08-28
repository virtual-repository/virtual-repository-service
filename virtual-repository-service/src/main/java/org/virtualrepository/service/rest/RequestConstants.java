/**
 * (c) 2013 FAO / UN (project: virtual-repository-service)
 */
package org.virtualrepository.service.rest;

import javax.ws.rs.core.MediaType;

/**
 * Place your class / interface description here.
 *
 * History:
 *
 * ------------- --------------- -----------------------
 * Date			 Author			 Comment
 * ------------- --------------- -----------------------
 * 28 Aug 2013   Fiorellato     Creation.
 *
 * @version 1.0
 * @since 28 Aug 2013
 */
public interface RequestConstants {
	String APPLICATION_VXML 			 = "application/vr-xml";
	String APPLICATION_SDMX_GENERIC_DATA = "application/vnd.sdmx.genericdata+xml;version=2.1";
	
	String[] PREFERRED_MEDIA_TYPES = { 
		APPLICATION_VXML,
		MediaType.APPLICATION_JSON,
		MediaType.APPLICATION_XML
	};
	
	String ASSET_TYPE_QUERY_PARAMETER = "assetType";
	String MODEL_QUERY_PARAMETER 	  = "model";
}
