package org.virtualrepository.service;


/**
 * Service-wide constants.
 * 
 * @author Fabio Simeoni
 * 
 */
public class Constants {

	/**
	 * The name of the internal configuration file.
	 */
	public static final String config_internal_file = "configuration.properties";
	
	
	/**
	 * The name of the external configuration file.
	 */
	public static final String config_external_file = "vrs.properties";

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
	public static final int default_ttl = 3600;
	
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
	public static final String xobject = "application/vnd.vr.asset+vxml;version=1";
	
	/**
	 * JSon-based exchange media type.
	 */
	public static final String jmom = "application/vnd.vr.asset+json;version=1";
	
	/**
	 * XML-based exchange media type.
	 */
	public static final String xmom = "application/vnd.vr.asset+xml;version=1";

	/**
	 * SMDXML-based exchange media type.
	 */
	public static final String sdmx_ml = "application/vnd.sdmx.structure+xml;version=2.1";
	
	
	/**
	 * JSon-based exchange media type for tables.
	 */
	public static final String jtable = "application/vnd.vr.tabular+json;version=1";
	
	/**
	 * Xml-based exchange media type for tables.
	 */
	public static final String xtable = "application/vnd.vr.tabular+xml;version=1";
	
	
	/**
	 * Xml-based serialisation media type for tables.
	 */
	public static final String vtable = "application/vnd.vr.tabular+vxml;version=1";
	
	
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
