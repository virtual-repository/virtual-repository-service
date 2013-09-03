package org.virtualrepository.service;

import static javax.ws.rs.core.MediaType.*;

/**
 * Service-wide constants.
 * 
 * @author Fabio Simeoni
 * 
 */
public class Constants {

	/**
	 * The name of the org.virtualrepository.service.configuration file
	 */
	public static final String configFile = "configuration.properties";

	/**
	 * The name of the org.virtualrepository.service.configuration property with the known asset types.
	 */
	public static final String configTypesProperty = "asset-types";

	/**
	 * The name of the org.virtualrepository.service.configuration property with the name of the service endpoint.
	 */
	public static final String config_endpoint_name = "service-name";

	/**
	 * The name of the org.virtualrepository.service.configuration property with the version of the virtual repository.
	 */
	public static final String config_virtual_repository = "virtual-repository";

	/**
	 * Custom XML serialisation media type.
	 */
	public static final String APPLICATION_VXML = "application/vr-xml";

	/**
	 * Suffix for secondary media types.
	 */
	public static final String SECONDARY = ";q=0.9";

	/**
	 * Custom XML serialisation media type.
	 */
	public static final String APPLICATION_SDMX_GENERIC_DATA = "application/vnd.sdmx.genericdata+xml;version=2.1";

	/**
	 * Custom XML serialisation media type.
	 */
	public static final String[] PREFERRED_MEDIA_TYPES = { APPLICATION_VXML, APPLICATION_JSON, APPLICATION_XML };

	
	public static String ASSET_TYPE_QUERY_PARAMETER = "assetType";
	public static String MODEL_QUERY_PARAMETER = "model";

	public static String TABLE = "table";
	public static String SDMX_SS = "sdmx-ss";

}
