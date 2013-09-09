package org.virtualrepository.service;

import javax.ws.rs.core.MediaType;

/**
 * Service-wide constants.
 * 
 * @author Fabio Simeoni
 * 
 */
public class Constants {

	/**
	 * The name of the configuration file.
	 */
	public static final String configFile = "configuration.properties";

	/**
	 * The name of the configuration property with the known asset types.
	 */
	public static final String config_types_name = "asset-types";

	/**
	 * The name of the configuration property with the name of the service endpoint.
	 */
	public static final String config_endpoint_name = "service-name";
	
	/**
	 * The default time-to-leave of responses.
	 */
	public static final int default_ttl_ = 3600;
	
	/**
	 * The name of the configuration property with the time-to-live of responses.
	 */
	public static final String config_ttl_name = "response-ttl";
	
	
	/**
	 * The name of the configuration property with the time-to-live of responses.
	 */
	public static final String config_repositories_name = "repositories";

	/**
	 * The name of the configuration property with the version of the virtual repository.
	 */
	public static final String config_virtual_repository = "virtual-repository";

	/**
	 * XML-based serialisation media type.
	 */
	public static final String xobject = "application/vr.v1+xml";
	
	/**
	 * JSon-based exchange media type.
	 */
	public static final String jmom = MediaType.APPLICATION_JSON;
	
	/**
	 * XML-based exchange media type.
	 */
	public static final String xmom = MediaType.APPLICATION_XML;

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
	public static final String[] ALL_MEDIA_TYPES = {jmom,xmom,xobject};

	
	public static String ASSET_TYPE_QUERY_PARAMETER = "assetType";
	public static String MODEL_QUERY_PARAMETER = "model";

	public static String TABLE = "table";
	public static String SDMX_SS = "sdmx-ss";

}
